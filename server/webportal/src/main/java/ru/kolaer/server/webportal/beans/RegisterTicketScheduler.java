package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    private TypeServer typeServer;
    private TicketRegisterDao ticketRegisterDao;
    private JavaMailSender mailSender;
    private SimpleMailMessage mailMessage;
    private BankAccountDao bankAccountDao;

    public RegisterTicketScheduler(TypeServer typeServer,
                                   TicketRegisterDao ticketRegisterDao,
                                   JavaMailSender mailSender,
                                   SimpleMailMessage mailMessage,
                                   BankAccountDao bankAccountDao) {
        this.typeServer = typeServer;
        this.ticketRegisterDao = ticketRegisterDao;
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
        this.bankAccountDao = bankAccountDao;
    }

    @PostConstruct
    public void init() {
        this.emails.add("oit@kolaer.ru");
    }

    //@Scheduled(cron = "0 0 14 * * *", zone = "Europe/Moscow")
    public void generateAddTicketsScheduled() {
        if(!typeServer.isTest())
            this.generateAddTicketDocument();
    }

    @Scheduled(cron = "0 0 9 26-31 * ?", zone = "Europe/Moscow")
    public void generateZeroTicketsLastDayOfMonthScheduled() {
        if(!typeServer.isTest()) {
            final LocalDate now = LocalDate.now();
            final LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
            final int lastDay = end.getDayOfWeek().getValue() == 7
                    || end.getDayOfWeek().getValue() == 6 ? 6 :  end.getDayOfMonth();

            if (now.getDayOfMonth() == lastDay) {
                this.generateZeroTicketDocument();
                this.generateDefaultTicketDocument();
            }
        }
    }

    public boolean generateAddTicketDocument() {
        final List<TicketRegisterEntity> allOpenRegister = ticketRegisterDao.findAllOpenRegister();
        if(allOpenRegister.size() > 0) {
            allOpenRegister.forEach(ticketRegister -> ticketRegister.setClose(true));
            ticketRegisterDao.update(allOpenRegister);

            List<TicketEntity> allTickets = new ArrayList<>();
            allOpenRegister.stream().filter(t -> t.getTickets() != null).map(TicketRegisterEntity::getTickets)
                    .forEach(allTickets::addAll);

            if(this.sendMail(allTickets, "IMMEDIATE", "DR", "Сформированные талоны ЛПП для зачисления. Файл во вложении!")) {
                this.lastSend = LocalDateTime.now();
                return true;
            }
        }
        return false;
    }

    public boolean generateZeroTicketDocument() {
        final LocalDateTime now = LocalDateTime.now();
        final String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss")
                .format(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 0).plusMonths(1));
        return this.generateSetTicketDocument(0, String.format("IN-TIME  %s", dateToUpdate), "ZR",
                "Сформированные талоны ЛПП для обнуления. Файл во вложении!");
    }

    public boolean generateDefaultTicketDocument() {
        final LocalDateTime now = LocalDateTime.now();
        final String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss")
                .format(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 10).plusMonths(1));
        return this.generateSetTicketDocument(25, String.format("IN-TIME  %s", dateToUpdate), "DR",
                "Сформированные талоны ЛПП для зачисления. Файл во вложении!");
    }

    public boolean generateSetTicketDocument(Integer count, String header, String typeTicket, String textMail) {
        List<TicketEntity> allTiskets = this.bankAccountDao.findAll().stream().map(bankAccount -> {
            final TicketEntity ticket = new TicketEntity();
            ticket.setEmployee(bankAccount.getEmployeeEntity());
            ticket.setCount(count);
            return ticket;
        }).collect(Collectors.toList());

        return this.sendMail(allTiskets, header, typeTicket, textMail);
    }

    private boolean sendMail(List<TicketEntity> tickets, String header, String typeTickets, String text) {
        if (tickets.size() > 0) {
            try {
                final File genFile = this.generateTextFile(tickets, header, typeTickets, text);
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

    private File generateTextFile(List<TicketEntity> tickets, String header, String type, String text) throws IOException {
        final LocalDateTime now = LocalDateTime.now();
        String fileName = "tickets/Z001000.KOLAER_ENROLL0010001." + String.format("%03d",now.getDayOfYear());
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
            printWriter.printf("H %s %s %s", dateTime[0], dateTime[1], header);
            printWriter.printf(System.lineSeparator());
            int countTickets = 0;
            for(final TicketEntity ticket : tickets) {
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
