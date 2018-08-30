package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.servirces.VacationService;

import java.util.List;

@RestController
@RequestMapping(value = "/vacations")
@Api(tags = "Отпуска")
@Slf4j
public class VacationController {
    private final VacationService vacationService;

    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @ApiOperation(value = "Получить отпуска")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<VacationDto> getVacations(@ModelAttribute FindVacationPageRequest request) {
        return vacationService.getVacations(request);
    }

    @ApiOperation(value = "Добавить отпуск")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationDto addVacations(@RequestBody VacationDto request) {
        return vacationService.addVacation(request);
    }

    @ApiOperation(value = "Редактировать отпуск")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationDto updateVacations(@PathVariable("id") Long vacationId,
                                       @RequestBody VacationDto request) {
        return vacationService.updateVacation(vacationId, request);
    }

    @ApiOperation(value = "Удалить отпуск")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteVacations(@PathVariable("id") Long vacationId) {
        vacationService.deleteVacation(vacationId);
    }

    @ApiOperation(value = "Получить периоды")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/periods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<VacationPeriodDto> getVacationPeriods(@ModelAttribute FindVacationPeriodPageRequest request) {
        return vacationService.getVacationPeriods(request);
    }

    @ApiOperation(value = "Получить баланс")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/balance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationBalanceDto getBalance(@ModelAttribute FindBalanceRequest request) {
        return vacationService.getBalance(request);
    }

    @ApiOperation(value = "Расчитать дней отпуска между датами")
    @UrlDeclaration
    @RequestMapping(value = "/calculate/days", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationCalculateDto getCalculateDaysBetweenDate(@RequestBody VacationCalculateDaysRequest request) {
        return vacationService.vacationCalculate(request);
    }

    @ApiOperation(value = "Расчитать дату отпуска между началом даты и дней")
    @UrlDeclaration
    @RequestMapping(value = "calculate/date", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationCalculateDto getCalculateDaysBetweenDate(@RequestBody VacationCalculateDateRequest request) {
        return vacationService.vacationCalculate(request);
    }

    @ApiOperation(value = "Сгенерировать отчет для календаря")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/report/calendar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VacationReportCalendarEmployeeDto> generateVacationReportCalendar(@ModelAttribute GenerateReportCalendarRequest request) {
        return vacationService.generateReportCalendar(request);
    }

    @ApiOperation(value = "Сгенерировать отчет для распределения")
    @UrlDeclaration(isUser = false, isVacationAdmin = true, isVacationDepEdit = true)
    @RequestMapping(value = "/report/distribute", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VacationReportDistributeDto generateVacationReportDistribute(@ModelAttribute GenerateReportDistributeRequest request) {
        return vacationService.generateReportDistribute(request);
    }

}
