package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.DateTimeJson;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.FilterType;
import ru.kolaer.server.webportal.mvc.model.dto.HolidayDto;
import ru.kolaer.server.webportal.mvc.model.dto.HolidayFilter;
import ru.kolaer.server.webportal.mvc.model.dto.HolidaySort;
import ru.kolaer.server.webportal.mvc.model.servirces.HolidayService;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by danilovey on 31.10.2016.
 */
@RestController
@Api(tags = "Праздники", description = "Праздники в России")
public class HolidaysController {
    private final HolidayService holidayService;

    @Autowired
    public HolidaysController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @ApiOperation(value = "Получить все праздники")
    @UrlDeclaration(description = "Получить все праздники", isAccessAll = true)
    @RequestMapping(value = "/non-security/holidays", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<HolidayDto> getAll(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                   @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                   HolidaySort sortParam,
                                   HolidayFilter filter) {
        return this.holidayService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Добавить праздник")
    @UrlDeclaration(description = "Добавить праздник", requestMethod = RequestMethod.POST, isUser = false, isOk = true)
    @RequestMapping(value = "/holidays", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HolidayDto addHoliday(@RequestBody HolidayDto holidayDto) {
        return this.holidayService.add(holidayDto);
    }

    @ApiOperation(value = "Редактировать праздник")
    @UrlDeclaration(description = "Редактировать праздник", requestMethod = RequestMethod.PUT, isUser = false, isOk = true)
    @RequestMapping(value = "/holidays/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HolidayDto updateHoliday(@PathVariable("id") Long id, @RequestBody HolidayDto holidayDto) {
        return this.holidayService.update(id, holidayDto);
    }

    @ApiOperation(value = "Удалить праздник")
    @UrlDeclaration(description = "Удалить праздник", requestMethod = RequestMethod.DELETE, isUser = false, isOk = true)
    @RequestMapping(value = "/holidays/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateHoliday(@PathVariable("id") Long id) {
        this.holidayService.delete(id);
    }

    @ApiOperation(value = "Получить все праздники")
    @UrlDeclaration(description = "Получить все праздники", isAccessAll = true)
    @RequestMapping(value = "/non-security/holidays/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public List<Holiday> getHolidaysAll() {
        return this.holidayService.getAllHolidays();
    }

    @ApiOperation(
            value = "Получить праздники в месяце"
    )
    @UrlDeclaration(description = "Получить праздники в месяце", isAccessAll = true)
    @RequestMapping(value = "/non-security/holidays/get/{month}/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Deprecated
    public List<Holiday> getPublicHolidays(
            @ApiParam(value = "Номер месяца", required = true) @PathVariable final String month,
            @ApiParam(value = "Номер года", required = true) @PathVariable final String year) {
        final DateTimeJson dateTimeJson = new DateTimeJson("01." + month + "." + year, "00:00:00");
        return holidayService.getHolidayByMonth(dateTimeJson);
    }

    @ApiOperation(value = "Получить все праздники в году")
    @UrlDeclaration(description = "Получить все праздники в году", isAccessAll = true)
    @RequestMapping(value = "/non-security/holidays/get/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<HolidayDto> getAllInMonth(@PathVariable("year") Long year) {
        HolidayFilter holidayFilter = new HolidayFilter();
        holidayFilter.setFilterHolidayDate(LocalDate.of(year.intValue(), 1, 1));
        holidayFilter.setTypeFilterHolidayDate(FilterType.LESS);

        return this.holidayService.getAll(new HolidaySort(), holidayFilter);
    }

}
