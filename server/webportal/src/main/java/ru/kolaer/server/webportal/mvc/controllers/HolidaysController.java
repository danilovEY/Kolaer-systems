package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.HolidayService;

import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
@RestController
@RequestMapping(value = "/non-security/holidays")
@Api(tags = "Праздники", description = "Праздники в России")
public class HolidaysController extends BaseController {
    private final HolidayService holidayService;

    @Autowired
    public HolidaysController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @ApiOperation(
            value = "Получить все праздники"
    )
    @UrlDeclaration(description = "Получить все праздники", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Holiday> getHolidaysAll() {
        return this.holidayService.getAllHolidays();
    }

    @ApiOperation(
            value = "Получить праздники в месяце"
    )
    @UrlDeclaration(description = "Получить праздники в месяце", isAccessAll = true)
    @RequestMapping(value = "/get/{month}/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Holiday> getPublicHolidays(
            @ApiParam(value = "Номер месяца", required = true) @PathVariable final String month,
            @ApiParam(value = "Номер года", required = true) @PathVariable final String year) {
        final DateTimeJson dateTimeJson = new DateTimeJson("01." + month + "." + year, "00:00:00");
        return holidayService.getHolidayByMonth(dateTimeJson);
    }

}
