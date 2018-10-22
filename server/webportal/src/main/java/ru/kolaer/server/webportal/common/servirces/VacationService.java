package ru.kolaer.server.webportal.common.servirces;

import org.springframework.http.ResponseEntity;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.microservice.vacation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface VacationService {

    Page<VacationDto> getVacations(FindVacationPageRequest request);

    Page<VacationPeriodDto> getVacationPeriods(FindVacationPeriodPageRequest request);

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
