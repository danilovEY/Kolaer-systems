package ru.kolaer.server.vacation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.VacationAccessConstant;
import ru.kolaer.common.dto.Page;
import ru.kolaer.server.vacation.model.dto.*;
import ru.kolaer.server.vacation.model.request.*;
import ru.kolaer.server.vacation.service.GenerateCalendarReportForVacationService;
import ru.kolaer.server.vacation.service.VacationService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Api(tags = "Отпуска")
@Slf4j
@Validated
public class VacationController {
    private final VacationService vacationService;
    private final GenerateCalendarReportForVacationService generateCalendarReportForVacationService;

    @Autowired
    public VacationController(VacationService vacationService,
                              GenerateCalendarReportForVacationService generateCalendarReportForVacationService) {
        this.vacationService = vacationService;
        this.generateCalendarReportForVacationService = generateCalendarReportForVacationService;
    }

    @ApiOperation(value = "Получить отпуска")
    @GetMapping(RouterConstants.VACATIONS)
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_GET + "','" + VacationAccessConstant.VACATIONS_GET_DEPARTMENT + "')")
    public Page<VacationDto> getVacations(@ModelAttribute FindVacationPageRequest request) {
        return vacationService.getVacations(request);
    }

    @ApiOperation(value = "Добавить отпуск")
    @PostMapping(RouterConstants.VACATIONS)
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_ADD + "','" + VacationAccessConstant.VACATIONS_ADD_DEPARTMENT + "')")
    public VacationDto addVacations(@RequestBody VacationDto request) {
        return vacationService.addVacation(request);
    }

    @ApiOperation(value = "Редактировать отпуск")
    @PutMapping(RouterConstants.VACATIONS_ID)
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_EDIT + "','" + VacationAccessConstant.VACATIONS_EDIT_DEPARTMENT + "')")
    public VacationDto updateVacations(@PathVariable(PathVariableConstants.VACATION_ID) @Min(1) long vacationId,
            @RequestBody VacationDto request) {
        return vacationService.updateVacation(vacationId, request);
    }

    @ApiOperation(value = "Удалить отпуск")
    @DeleteMapping(RouterConstants.VACATIONS_ID)
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_DELETE + "','" + VacationAccessConstant.VACATIONS_DELETE_DEPARTMENT + "')")
    public void deleteVacations(@PathVariable(PathVariableConstants.VACATION_ID) @Min(1) long vacationId) {
        vacationService.deleteVacation(vacationId);
    }

    @ApiOperation(value = "Получить периоды")
    @GetMapping(RouterConstants.VACATIONS_PERIODS)
    public Page<VacationPeriodDto> getVacationPeriods(@ModelAttribute FindVacationPeriodPageRequest request) {
        return vacationService.getVacationPeriods(request);
    }

    @ApiOperation(value = "Получить баланс")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_BALANCE_GET + "','" + VacationAccessConstant.VACATIONS_BALANCE_GET_DEPARTMENT + "')")
    @GetMapping(RouterConstants.VACATIONS_BALANCE)
    public VacationBalanceDto getBalance(@ModelAttribute FindBalanceRequest request) {
        return vacationService.getBalance(request);
    }

    @ApiOperation(value = "Изменить баланс")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_BALANCE_EDIT + "')")
    @PutMapping(RouterConstants.VACATIONS_BALANCE)
    public VacationBalanceDto getBalance(@RequestBody VacationBalanceDto balance) {
        return vacationService.updateVacationBalance(balance);
    }

    @ApiOperation(value = "Расчитать дней отпуска между датами")
    @PostMapping(RouterConstants.VACATIONS_CALCULATE_DAYS)
    public VacationCalculateDto getCalculateDaysBetweenDate(@RequestBody VacationCalculateDaysRequest request) {
        return vacationService.vacationCalculate(request);
    }

    @ApiOperation(value = "Расчитать дату отпуска между началом даты и дней")
    @PostMapping(RouterConstants.VACATIONS_CALCULATE_DATE)
    public VacationCalculateDto getCalculateDaysBetweenDate(@RequestBody VacationCalculateDateRequest request) {
        return vacationService.vacationCalculate(request);
    }

    @ApiOperation(value = "Сгенерировать отчет для календаря")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_GET + "','" + VacationAccessConstant.VACATIONS_GET_DEPARTMENT + "')")
    @GetMapping(RouterConstants.VACATIONS_REPORT_CALENDAR_EXPORT)
    public ResponseEntity generateVacationReportCalendar(@ModelAttribute GenerateReportCalendarRequest request, HttpServletResponse response) {
        return generateCalendarReportForVacationService.generateCalendarReportExtort(request, response);
    }

    @ApiOperation(value = "Сгенерировать отчет и скачать для календаря")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_GET + "','" + VacationAccessConstant.VACATIONS_GET_DEPARTMENT + "')")
    @GetMapping(RouterConstants.VACATIONS_REPORT_CALENDAR)
    public List<VacationReportCalendarEmployeeDto> generateVacationReportCalendar(@ModelAttribute GenerateReportCalendarRequest request) {
        return vacationService.generateReportCalendar(request);
    }

    @ApiOperation(value = "Сгенерировать отчет для распределения")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_GET + "','" + VacationAccessConstant.VACATIONS_GET_DEPARTMENT + "')")
    @GetMapping(RouterConstants.VACATIONS_REPORT_DISTRIBUTE)
    public VacationReportDistributeDto generateVacationReportDistribute(@ModelAttribute GenerateReportDistributeRequest request) {
        return vacationService.generateReportDistribute(request);
    }

    @ApiOperation(value = "Сгенерировать отчет для соотношений")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_GET + "','" + VacationAccessConstant.VACATIONS_GET_DEPARTMENT + "')")
    @GetMapping(RouterConstants.VACATIONS_REPORT_TOTAL_COUNT)
    public List<VacationReportPipeDto> generateVacationReportTotalCount(@ModelAttribute GenerateReportTotalCountRequest request) {
        return vacationService.generateReportTotalCount(request);
    }

    @ApiOperation(value = "Сгенерировать отчет в эксель")
    @PreAuthorize("hasAnyRole('" + VacationAccessConstant.VACATIONS_GET + "','" + VacationAccessConstant.VACATIONS_GET_DEPARTMENT + "')")
    @GetMapping(RouterConstants.VACATIONS_REPORT_EXPORT)
    public ResponseEntity generateVacationReportExport(@ModelAttribute GenerateReportExportRequest request,
                                                       HttpServletResponse response) {
        return vacationService.generateReportExport(request, response);
    }

}
