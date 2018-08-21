package ru.kolaer.server.webportal.mvc.model.dao;


import ru.kolaer.server.webportal.mvc.model.dto.vacation.FindBalanceRequest;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.FindVacationPageRequest;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.FindVacationPeriodPageRequest;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationBalanceEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationPeriodEntity;

import java.util.List;

public interface VacationDao extends DefaultDao<VacationEntity> {
    long findCountVacation(FindVacationPageRequest request);

    List<VacationEntity> findAllVacation(FindVacationPageRequest request);

    VacationBalanceEntity findBalance(FindBalanceRequest request);

    Long findCountPeriods(FindVacationPeriodPageRequest request);

    List<VacationPeriodEntity> findAllPeriods(FindVacationPeriodPageRequest request);

    VacationPeriodEntity findLastPeriods(FindVacationPeriodPageRequest request);
    VacationPeriodEntity findPeriodsByYear(long year);

    VacationBalanceEntity save(VacationBalanceEntity vacationBalanceEntity);
}
