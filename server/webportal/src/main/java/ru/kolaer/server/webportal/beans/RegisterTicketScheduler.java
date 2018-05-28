package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.mvc.model.dto.SendTicketDto;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.UploadFileService;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 13.12.2016.
 */
@Component
@Slf4j
public class RegisterTicketScheduler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private final List<String> emails = new ArrayList<>();

    private final UploadFileService uploadFileService;
    private JavaMailSender mailSender;
    private SimpleMailMessage mailMessage;
    private final Environment environment;

    public RegisterTicketScheduler(UploadFileService uploadFileService,
                                   JavaMailSender mailSender,
                                   SimpleMailMessage mailMessage,
                                   Environment environment) {
        this.uploadFileService = uploadFileService;
        this.mailSender = mailSender;
        this.mailMessage = mailMessage;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        this.emails.add(environment.getProperty("tickets.email"));
    }

    //@Scheduled(cron = "0 0 14 * * *", zone = "Europe/Moscow")
//    public void generateAddTicketsScheduled() {
//        if(!typeServer.isTest())
//            this.generateAddTicketDocument();
//    }

//    @Scheduled(cron = "0 0 8 26-31 * ?", zone = "Europe/Moscow")
//    @Transactional(readOnly = true)
//    public void generateZeroTicketsLastDayOfMonthScheduled() {
//        if(!typeServer.isTest()) {
//            final LocalDate now = LocalDate.now();
//            final LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
//            final int lastDay = end.getDayOfWeek().getValue() == 7
//                    || end.getDayOfWeek().getValue() == 6 ? 6 :  end.getDayOfMonth();
//
//            if (now.getDayOfMonth() == lastDay) {
//                List<SendTicketDto> allTickets = getAllTickets();
//                this.generateZeroTicketDocument(allTickets);
//                this.generateDefaultTicketDocument(allTickets);
//            }
//        }
//    }

//    public boolean generateAddTicketDocument() {
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
//        return false;
//    }

    public UploadFileEntity generateReportTickets(List<SendTicketDto> tickets, LocalDateTime inTime) {
        String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss").format(inTime);

        return this.generateTextFile(tickets, String.format("IN-TIME  %s", dateToUpdate));

//        return this.sendMail(tickets, ,
//                "Сформированные талоны ЛПП и время исполнения файла: " + dateToUpdate + ". Файл во вложении!");
    }

    public UploadFileEntity generateReportTickets(List<SendTicketDto> tickets) {
        return this.generateTextFile(tickets, "IMMEDIATE");
    }

//    public boolean generateZeroTicketDocument(List<SendTicketDto> allTickets) {
//        final LocalDateTime now = LocalDateTime.now();
//        final String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss")
//                .format(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 0).plusMonths(1));
//
//        for (SendTicketDto ticket : allTickets) {
//            ticket.setCount(0);
//            ticket.setTypeOperation(TypeOperation.ZR);
//        }
//
//        return this.generateSetTicketDocument(allTickets, String.format("IN-TIME  %s", dateToUpdate), "ZR",
//                "Сформированные талоны ЛПП для обнуления. Файл во вложении!");
//    }
//
//    public boolean generateDefaultTicketDocument(List<SendTicketDto> allTickets) {
//        final LocalDateTime now = LocalDateTime.now();
//        final String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss")
//                .format(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 10).plusMonths(1));
//
//        for (SendTicketDto ticket : allTickets) {
//            ticket.setCount(25);
//            ticket.setTypeOperation(TypeOperation.DR);
//        }
//
//        return this.generateSetTicketDocument(allTickets, String.format("IN-TIME  %s", dateToUpdate), "DR",
//                "Сформированные талоны ЛПП для зачисления. Файл во вложении!");
//    }
//
////    @Transactional(readOnly = true)
////    public boolean generateSetTicketDocument(List<SendTicketDto> allTickets, String header, String typeTicket, String textMail) {
////        return this.sendMail(allTickets, header, typeTicket, textMail);
////    }
//
//    private List<SendTicketDto> getAllTickets() {
//        return this.bankAccountDao.findAll().stream()
//                .filter(bankAccountEntity -> bankAccountEntity.getEmployeeId() != null)
//                .map(bankAccount -> {
//                    SendTicketDto ticket = new SendTicketDto();
//                    ticket.setInitials(bankAccount.getEmployeeEntity().getInitials().toUpperCase());
//                    ticket.setCheck(bankAccount.getCheck());
//                    return ticket;
//                }).collect(Collectors.toList());
//    }

    public boolean sendMail(UploadFileEntity uploadFileEntity, String text) {
        if (uploadFileEntity != null) {
            this.mailSender.send(mimeMessage -> {
                Resource resource = uploadFileService.loadFile(uploadFileEntity.getPath());

                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                messageHelper.setFrom(mailMessage.getFrom());
                messageHelper.setTo(this.emails.toArray(new String[this.emails.size()]));
                messageHelper.setSubject("Сформированные талоны ЛПП");
                messageHelper.setText(text);
                messageHelper.addAttachment(uploadFileEntity.getFileName(), resource);
            });
            return true;
        }

        return false;
    }

    private UploadFileEntity generateTextFile(List<SendTicketDto> tickets, String header) {
        LocalDateTime now = LocalDateTime.now();

        String[] dateTime = dateTimeFormatter.format(now).split("-");

        int index = 1;

        UploadFileEntity uploadFileEntity = null;

        while (uploadFileEntity == null) {
            uploadFileEntity = uploadFileService
                    .createFile("tickets", String.format("Z001000.KOLAER_ENROLL001%04d.%03d", index++, now.getDayOfYear()), false);
        }
        String absolutePath = this.uploadFileService.getAbsolutePath(uploadFileEntity);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(absolutePath, "windows-1251");
            printWriter.printf("H %s %s %s", dateTime[0], dateTime[1], header);
            printWriter.printf(System.lineSeparator());
            for(final SendTicketDto ticket : tickets) {
                String initials = ticket.getInitials().toUpperCase();
                printWriter.printf("%s%" + String.valueOf(116 - initials.length()) + "s              %s%s",
                        initials,
                        ticket.getCheck(),
                        ticket.getTypeOperation().name(),
                        ticket.getCount());
                printWriter.printf(System.lineSeparator());
            }
            printWriter.printf("T         %10d", tickets.size());
            printWriter.printf(System.lineSeparator());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error("Файл {} не найден!", uploadFileEntity.getPath());
        } finally {
            if(printWriter != null)
                printWriter.close();
        }

        return uploadFileEntity;
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
