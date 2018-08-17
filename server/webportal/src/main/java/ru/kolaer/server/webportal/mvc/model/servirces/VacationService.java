package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;

public interface VacationService {

    @Transactional(readOnly = true)
    Page<VacationDto> getVacations(FindVacationPageRequest request);

    @Transactional(readOnly = true)
    Page<VacationPeriodDto> getVacationPeriods(FindVacationPeriodPageRequest request);

    VacationBalanceDto getBalance(FindBalanceRequest request);

    @Transactional(readOnly = true)
    VacationCalculateDto vacationCalculate(VacationCalculateRequest request);
}
