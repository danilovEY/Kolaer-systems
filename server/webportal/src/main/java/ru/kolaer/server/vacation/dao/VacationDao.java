package ru.kolaer.server.vacation.dao;


import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.vacation.model.entity.*;
import ru.kolaer.server.vacation.model.request.*;

import java.util.List;

public interface VacationDao extends DefaultDao<VacationEntity> {
    long findCountVacation(FindVacationPageRequest request);

    List<VacationEntity> findAllVacation(FindVacationPageRequest request);

    VacationBalanceEntity findBalance(FindBalanceRequest request);

    Long findCountPeriods(FindVacationPeriodPageRequest request);

    List<VacationPeriodEntity> findAllPeriods(FindVacationPeriodPageRequest request);

    VacationPeriodEntity findLastPeriods(FindVacationPeriodPageRequest request);
    VacationPeriodEntity findPeriodsByYear(int year);

    VacationBalanceEntity save(VacationBalanceEntity vacationBalanceEntity);

    List<VacationEntity> findAll(GenerateReportCalendarRequest request);

    VacationTotalCountEntity findAllVacationTotalCount(GenerateReportTotalCountRequest request);

    long findVacationTotalCount(GenerateReportTotalCountRequest request);

    List<VacationTotalCountDepartmentEntity> findVacationTotalCountDepartment(GenerateReportTotalCountRequest request);

    List<VacationEntity> findAll(GenerateReportDistributeRequest request);

    long findCountVacation(FindVacationByDepartmentRequest findVacation);

    List<VacationEntity> findAll(GenerateReportExportRequest request);
}
