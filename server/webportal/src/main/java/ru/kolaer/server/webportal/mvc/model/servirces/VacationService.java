package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;

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
}
