package ru.kolaer.server.webportal.beans;

import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 13.12.2016.
 */
@Component
@Slf4j
public class RegisterTicketScheduler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private final List<String> emails = new ArrayList<>();
    private final int INDEX = 116;
    private LocalDateTime lastSend;
    private boolean send = false;
    private boolean test = true;

    @Resource
    private Environment env;

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
        this.emails.add("bondarenkora@kolaer.ru");

        if(env.getRequiredProperty("test").equals("false")) {
            this.test = false;
        }
    }

    @Scheduled(cron = "0 0 15 * * *")
    public void generateTicketDocument() {
        if(test)
            return;

        if(send) {
            final List<TicketRegister> allOpenRegister = this.ticketRegisterService.getAllOpenRegister();
            if(allOpenRegister.size() > 0) {
                allOpenRegister.forEach(ticketRegister -> ticketRegister.setClose(true));
                this.ticketRegisterService.update(allOpenRegister);

                List<Ticket> allTiskets = new ArrayList<>();
                allOpenRegister.stream().filter(t -> t.getTickets() != null).map(TicketRegister::getTickets)
                        .forEach(allTiskets::addAll);

                if (allTiskets.size() > 0) {
                    try {
                        final File genFile = this.generateTextFile(allTiskets);
                        if (genFile != null) {
                            final FileInputStream fileInputStream = new FileInputStream(genFile);
                            this.emails.forEach(email ->
                                this.mailSender.send(mimeMessage -> {
                                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                                    messageHelper.setFrom(mailMessage.getFrom());
                                    messageHelper.setTo(email);
                                    messageHelper.setSubject("Сформированные талоны ЛПП");
                                    messageHelper.setText("Сформированные талоны ЛПП. Файл во вложении!");
                                    messageHelper.addAttachment(genFile.getName(), () -> fileInputStream);
                                })
                            );
                        }
                    } catch (IOException e) {
                        log.error("Ошибка при генерации фала!", e);
                    }
                }
            }
            this.lastSend = LocalDateTime.now();
            send = false;
        }
    }

    private File generateTextFile(List<Ticket> tickets) throws IOException {
        final LocalDateTime now = LocalDateTime.now();
        final String fileName = "Z001000.KOLAER_ENROLL0010001." + now.getDayOfYear();
        final String[] dateTime = dateTimeFormatter.format(now).split("-");

        final File file = new File(fileName);
        if(!file.exists() && !file.createNewFile())
            return null;

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileName, "Cp1251");
            printWriter.printf("H %s %s IMMEDIATE\n", dateTime[0], dateTime[1]);

            for(final Ticket ticket : tickets) {
                final String initials = ticket.getEmployee().getInitials().toUpperCase();
                printWriter.printf("%s%"+ String.valueOf(this.INDEX - initials.length()) +"s              DR%s\n", initials, bankAccountDao.findByInitials(initials), ticket.getCount());
            }
            printWriter.printf("T                 %d\n", tickets.size());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error("Файл {} не найден!", fileName);
        } finally {
            if(printWriter != null)
                printWriter.close();
        }

        return file;
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

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
        this.generateTicketDocument();
    }
}
