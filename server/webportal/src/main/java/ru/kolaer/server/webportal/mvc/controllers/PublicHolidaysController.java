package ru.kolaer.server.webportal.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.webportal.mvc.model.servirces.HolidayService;

/**
 * Created by danilovey on 31.10.2016.
 */
@RestController
@RequestMapping(value = "/non-security/holidays")
public class PublicHolidaysController {

    @Autowired
    private HolidayService holidayService;



}
