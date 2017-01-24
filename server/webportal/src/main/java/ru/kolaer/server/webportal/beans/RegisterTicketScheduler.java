package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.Ticket;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 13.12.2016.
 */
@Component
@Slf4j
public class RegisterTicketScheduler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private final List<String> emails = new ArrayList<>();
    private LocalDateTime lastSend;

    @Resource
    private TypeServer typeServer;

    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private BankAccountDao bankAccountDao;

    @PostConstruct
    public void init() {
        this.emails.add("oit@kolaer.ru");
    }

    //@Scheduled(cron = "0 0 14 * * *", zone = "Europe/Moscow")
    public void generateAddTicketsScheduled() {
        if(!typeServer.isTest())
            this.generateAddTicketDocument();
    }

    @Scheduled(cron = "0 0 13 26-31 * ?", zone = "Europe/Moscow")
    public void generateZeroTicketsLastDayOfMonthScheduled() {
        if(!typeServer.isTest()) {
            final LocalDate now = LocalDate.now();
            final LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
            final int lastDay = end.getDayOfWeek().getValue() == DateTimeConstants.SUNDAY
                    || end.getDayOfWeek().getValue() == DateTimeConstants.SATURDAY ? 6 :  end.getDayOfMonth();

            if (now.getDayOfMonth() == lastDay) {
                this.generateZeroTicketDocument();
            }
        }
    }

    public boolean generateAddTicketDocument() {
        final List<TicketRegister> allOpenRegister = this.ticketRegisterService.getAllOpenRegister();
        if(allOpenRegister.size() > 0) {
            allOpenRegister.forEach(ticketRegister -> ticketRegister.setClose(true));
            this.ticketRegisterService.update(allOpenRegister);

            List<Ticket> allTiskets = new ArrayList<>();
            allOpenRegister.stream().filter(t -> t.getTickets() != null).map(TicketRegister::getTickets)
                    .forEach(allTiskets::addAll);

            if(this.sendMail(allTiskets, "DR", "Сформированные талоны ЛПП для зачисления. Файл во вложении!")) {
                this.lastSend = LocalDateTime.now();
                return true;
            }
        }
        return false;
    }

    public boolean generateZeroTicketDocument() {
        return this.generateSetTicketDocument(0, "ZR", "Сформированные талоны ЛПП для обнуления. Файл во вложении!");
    }

    public boolean generateSetTicketDocument(Integer count, String typeTicket, String textMail) {
        List<Ticket> allTiskets = this.bankAccountDao.findAll().stream().map(bankAccount -> {
            final Ticket ticket = new Ticket();
            ticket.setEmployee(bankAccount.getEmployeeEntity());
            ticket.setCount(count);
            return ticket;
        }).collect(Collectors.toList());

        return this.sendMail(allTiskets, typeTicket, textMail);
    }

    private boolean sendMail(List<Ticket> tickets, String typeTiskets, String text) {
        if (tickets.size() > 0) {
            try {
                final File genFile = this.generateTextFile(tickets, typeTiskets, text);
                if (genFile != null) {
                    this.mailSender.send(mimeMessage -> {
                        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                        messageHelper.setFrom(mailMessage.getFrom());
                        messageHelper.setTo(this.emails.toArray(new String[this.emails.size()]));
                        messageHelper.setSubject("Сформированные талоны ЛПП");
                        messageHelper.setText(text);
                        messageHelper.addAttachment(genFile.getName(), () -> new FileInputStream(genFile));
                    });
                    return true;
                }
            } catch (IOException e) {
                log.error("Ошибка при генерации фала!", e);
            }
        }
        return false;
    }

    private File generateTextFile(List<Ticket> tickets, String type, String text) throws IOException {
        final LocalDateTime now = LocalDateTime.now();
        String fileName = "tickets/Z001000.KOLAER_ENROLL0010001." + now.getDayOfYear();
        final String[] dateTime = dateTimeFormatter.format(now).split("-");

        File dirTickets = new File("tickets");
        if(!dirTickets.exists())
            dirTickets.mkdir();

        File file = new File(fileName);
        if(file.exists()) {
            file.delete();
        }

        if(!file.createNewFile())
            return null;

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileName, "windows-1251");
            printWriter.printf("H %s %s IMMEDIATE", dateTime[0], dateTime[1]);
            printWriter.printf(System.lineSeparator());
            int countTickets = 0;
            for(final Ticket ticket : tickets) {
                final String initials = ticket.getEmployee().getInitials().toUpperCase();
                String check = bankAccountDao.findByInitials(initials).getCheck();
                if(check != null) {
                    printWriter.printf("%s%" + String.valueOf(116 - initials.length()) + "s              %s%s", initials, bankAccountDao.findByInitials(initials).getCheck(), type, ticket.getCount());
                    printWriter.printf(System.lineSeparator());
                    countTickets++;
                } else {
                    text += "\nВНИМАНИЕ! Для \"" + initials + "\" счет не найден!";
                }
            }
            printWriter.printf("T         %10d", countTickets);
            printWriter.printf(System.lineSeparator());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error("Файл {} не найден!", fileName);
        } finally {
            if(printWriter != null)
                printWriter.close();
        }

        return file;
    }

    public LocalDateTime getLastSend() {
        return lastSend;
    }

    public void addEmail(String email) {
        this.emails.add(email);
    }

    public void removeEmail(String email) {
        this.emails.remove(email);
    }

    public List<String> getEmails() {
        return this.emails;
    }

}
