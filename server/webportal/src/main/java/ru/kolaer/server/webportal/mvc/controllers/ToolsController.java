package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@RequestMapping("/non-security/tools")
public class ToolsController {

    @UrlDeclaration(description = "Получить серверное время",isAccessAll = true)
    @RequestMapping(value = "get/time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DateTimeJson getTime() {
        final DateTimeJson dateTimeJson = new DateTimeJson();
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        dateTimeJson.setDate(localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        dateTimeJson.setTime(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return dateTimeJson;
    }

    @UrlDeclaration(description = "Получить период до времени",isAccessAll = true)
    @RequestMapping(value = "get/period", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DateTimeJson getPeriod(DateTimeJson dateTimeJson) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeJson.getDate()+" "+dateTimeJson.getTime(), formatter);
        LocalDateTime tempDateTime = LocalDateTime.now();

        long years = tempDateTime.until(dateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(dateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(dateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);

        long hours = tempDateTime.until(dateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(dateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(dateTime, ChronoUnit.SECONDS);

        LocalDate localDate = LocalDate.of(Math.toIntExact(years),
                Math.toIntExact(months),
                Math.toIntExact(days));

        LocalTime localTime = LocalTime.of(Math.toIntExact(hours),
                Math.toIntExact(minutes),
                Math.toIntExact(seconds));

        final DateTimeJson result = new DateTimeJson();
        result.setDate(localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        result.setTime(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return result;
    }

    @UrlDeclaration(description = "Получить подробное серверное время",isAccessAll = true)
    @RequestMapping(value = "get/time/default", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LocalDateTime getDefaultTime() {
        return LocalDateTime.now();
    }
}
