package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@RequestMapping("/non-security/tools")
public class ToolsController {

    @RequestMapping(value = "get/time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DateTimeJson getTime() {
        final DateTimeJson dateTimeJson = new DateTimeJson();
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        dateTimeJson.setData(localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        dateTimeJson.setTime(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return dateTimeJson;
    }

}
