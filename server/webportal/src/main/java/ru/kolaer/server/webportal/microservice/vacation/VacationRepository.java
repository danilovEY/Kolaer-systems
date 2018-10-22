package ru.kolaer.server.webportal.microservice.vacation;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

public interface VacationRepository extends DefaultRepository<VacationEntity> {
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
