package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.mvc.model.dto.ticket.SendTicketDto;
import ru.kolaer.server.webportal.mvc.model.dto.upload.UploadFileDto;
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

    public UploadFileDto generateReportTickets(List<SendTicketDto> tickets, LocalDateTime inTime) {
        String dateToUpdate = DateTimeFormatter.ofPattern("yyyyMMdd hhmmss").format(inTime);

        return this.generateTextFile(tickets, String.format("IN-TIME  %s", dateToUpdate));
    }

    public UploadFileDto generateReportTickets(List<SendTicketDto> tickets) {
        return this.generateTextFile(tickets, "IMMEDIATE");
    }

    public boolean sendMail(UploadFileDto uploadFileEntity, String text) {
        if (uploadFileEntity != null) {
            this.mailSender.send(mimeMessage -> {
                Resource resource = uploadFileService.loadFile(uploadFileEntity.getPath(), uploadFileEntity.isAbsolutePath());

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

    private UploadFileDto generateTextFile(List<SendTicketDto> tickets, String header) {
        LocalDateTime now = LocalDateTime.now();

        String[] dateTime = dateTimeFormatter.format(now).split("-");

        int index = 1;

        UploadFileDto uploadFileEntity = null;

        while (uploadFileEntity == null) {
            uploadFileEntity = uploadFileService
                    .createFile("tickets", String.format("Z001000.KOLAER_ENROLL001%04d.%03d", index++, now.getDayOfYear()), false, true, false, true);
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
