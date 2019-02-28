package ru.kolaer.server.vacation.service;

import org.springframework.http.ResponseEntity;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.vacation.model.dto.*;
import ru.kolaer.server.vacation.model.request.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface VacationService {

    PageDto<VacationDto> getVacations(FindVacationPageRequest request);

    PageDto<VacationPeriodDto> getVacationPeriods(FindVacationPeriodPageRequest request);

    VacationBalanceDto getBalance(FindBalanceRequest request);

    VacationCalculateDto vacationCalculate(VacationCalculateDaysRequest request);
    VacationCalculateDto vacationCalculate(VacationCalculateDateRequest request);

    VacationDto addVacation(VacationDto request);

    VacationDto updateVacation(Long vacationId, VacationDto request);

    void deleteVacation(Long vacationId);

    List<VacationReportCalendarEmployeeDto> generateReportCalendar(GenerateReportCalendarRequest request);

    VacationReportDistributeDto generateReportDistribute(GenerateReportDistributeRequest request);

    List<VacationReportPipeDto> generateReportTotalCount(GenerateReportTotalCountRequest request);

    ResponseEntity generateReportExport(GenerateReportExportRequest request, HttpServletResponse response);

    VacationBalanceDto updateVacationBalance(VacationBalanceDto balance);
}
