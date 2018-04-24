package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.dto.SendTicketDto;

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
    private JavaMailSender mailSender;
    private SimpleMailMessage mailMessage;
    private BankAccountDao bankAccountDao;

    public RegisterTicketScheduler(TypeServer typeServer,
                                   JavaMailSender mailSender,
                                   SimpleMailMessage mailMessage,
                                   BankAccountDao bankAccountDao) {
        this.typeServer = typeServer;
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
        this.bankAccountDao = bankAccountDao;
    }

    @PostConstruct
    public void init() {
        this.emails.add("oit@kolaer.ru");
    }

    //@Scheduled(cron = "0 0 14 * * *", zone = "Europe/Moscow")
//    public void generateAddTicketsScheduled() {
//        if(!typeServer.isTest())
//            this.generateAddTicketDocument();
//    }

    @Scheduled(cron = "0 0 8 26-31 * ?", zone = "Europe/Moscow")
    @Transactional(readOnly = true)
    public void generateZeroTicketsLastDayOfMonthScheduled() {
        if(!typeServer.isTest()) {
            final LocalDate now = LocalDate.now();
            final LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
            final int lastDay = end.getDayOfWeek().getValue() == 7
                    || end.getDayOfWeek().getValue() == 6 ? 6 :  end.getDayOfMonth();

            if (now.getDayOfMonth() == lastDay) {
                List<SendTicketDto> allTickets = getAllTickets();
                this.generateZeroTicketDocument(allTickets);
                this.generateDefaultTicketDocument(allTickets);
            }
        }
    }

    public boolean generateAddTicketDocument() {
//        final List<TicketRegisterEntity> allOpenRegister = ticketRegisterDao.findAllOpenRegister();
//        if(allOpenRegister.size() > 0) {
//            allOpenRegister.forEach(ticketRegister -> ticketRegister.setClose(true));
//            ticketRegisterDao.update(allOpenRegister);
//
//            List<SendTicketDto> allTickets = new ArrayList<>();
//            allOpenRegister.stream()
//                    .filter(t -> t.getTickets() != null)
//                    .map(TicketRegisterEntity::getTickets)
//                    .map(ticketEntities -> ticketEntities.stream().map(ticketEntity -> {
//                        SendTicketDto sendTicketDto = new SendTicketDto();
//                        sendTicketDto.setInitials(ticketEntity.getEmployee().getInitials().toUpperCase());
//                        sendTicketDto.setCount(ticketEntity.getCount());
//                        sendTicketDto.setCheck(bankAccountDao.findByInitials(ticketEntity.getEmployee().getInitials()).getCheck());
//                        return sendTicketDto;
//                    }))
//                    .forEach(allTickets::addAll);
//
//            if(this.sendMail(allTickets, "IMMEDIATE", "DR", "Сформированные талоны ЛПП для зачисления. Файл во вложении!")) {
//                this.lastSend = LocalDateTime.now();
//                return true;
//            }
//        }
        return false;
    }

    public boolean generateZeroTicketDocument(List<SendTicketDto> allTickets) {
        final LocalDateTime now = LocalDateTime.now();
        final String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss")
                .format(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 0).plusMonths(1));

        for (SendTicketDto ticket : allTickets) {
            ticket.setCount(0);
        }

        return this.generateSetTicketDocument(allTickets, String.format("IN-TIME  %s", dateToUpdate), "ZR",
                "Сформированные талоны ЛПП для обнуления. Файл во вложении!");
    }

    public boolean generateDefaultTicketDocument(List<SendTicketDto> allTickets) {
        final LocalDateTime now = LocalDateTime.now();
        final String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss")
                .format(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 10).plusMonths(1));

        for (SendTicketDto ticket : allTickets) {
            ticket.setCount(25);
        }

        return this.generateSetTicketDocument(allTickets, String.format("IN-TIME  %s", dateToUpdate), "DR",
                "Сформированные талоны ЛПП для зачисления. Файл во вложении!");
    }

    @Transactional(readOnly = true)
    public boolean generateSetTicketDocument(List<SendTicketDto> allTickets, String header, String typeTicket, String textMail) {
        return this.sendMail(allTickets, header, typeTicket, textMail);
    }

    private List<SendTicketDto> getAllTickets() {
        return this.bankAccountDao.findAll().stream()
                .filter(bankAccountEntity -> bankAccountEntity.getEmployeeId() != null)
                .map(bankAccount -> {
                    SendTicketDto ticket = new SendTicketDto();
                    ticket.setInitials(bankAccount.getEmployeeEntity().getInitials().toUpperCase());
                    ticket.setCheck(bankAccount.getCheck());
                    return ticket;
                }).collect(Collectors.toList());
    }

    private boolean sendMail(List<SendTicketDto> tickets, String header, String typeTickets, String text) {
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

    private File generateTextFile(List<SendTicketDto> tickets, String header, String type, String text) throws IOException {
        File dirTickets = new File("tickets");
        if(!dirTickets.exists()) {
            dirTickets.mkdir();
        }

        LocalDateTime now = LocalDateTime.now();

        int index = 1;

        String fileName = "Z001000.KOLAER_ENROLL001000";
        String absoluteFileName = "tickets/" + fileName + String.valueOf(index) + String.format(".%03d",now.getDayOfYear());
        String[] dateTime = dateTimeFormatter.format(now).split("-");

        File file = new File(absoluteFileName);

        while (file.exists()) {
            absoluteFileName = "tickets/" + fileName + String.valueOf(++index) + String.format(".%03d",now.getDayOfYear());
            file = new File(absoluteFileName);
        }

        if(!file.createNewFile())
            return null;

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(absoluteFileName, "windows-1251");
            printWriter.printf("H %s %s %s", dateTime[0], dateTime[1], header);
            printWriter.printf(System.lineSeparator());
            int countTickets = 0;
            StringBuilder textBuilder = new StringBuilder(text);
            for(final SendTicketDto ticket : tickets) {
                String initials = ticket.getInitials().toUpperCase();
                String check = ticket.getCheck();
                if(check != null) {
                    printWriter.printf("%s%" + String.valueOf(116 - initials.length()) + "s              %s%s", initials, check, type, ticket.getCount());
                    printWriter.printf(System.lineSeparator());
                    countTickets++;
                } else {
                    textBuilder.append("\nВНИМАНИЕ! Для \"").append(initials).append("\" счет не найден!");
                }
            }
            text = textBuilder.toString();
            printWriter.printf("T         %10d", countTickets);
            printWriter.printf(System.lineSeparator());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error("Файл {} не найден!", absoluteFileName);
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
