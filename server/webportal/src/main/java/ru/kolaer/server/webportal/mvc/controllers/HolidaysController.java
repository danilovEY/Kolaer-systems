package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.server.webportal.mvc.model.servirces.HolidayService;

import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
@RestController
@RequestMapping(value = "/non-security/holidays")
public class HolidaysController {

    @Autowired
    private HolidayService holidayService;

    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Holiday> getHolidaysAll() {
        return this.holidayService.getAllHolidays();
    }

    @RequestMapping(value = "/get/{month}/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Holiday> getPublicHolidays(@PathVariable final String month, @PathVariable final String year) {
        final DateTimeJson dateTimeJson = new DateTimeJson("01." + month + "." + year, "00:00:00");
        return holidayService.getHolidayByMonth(dateTimeJson);
    }

}
