package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.VacationConverter;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.HolidayDao;
import ru.kolaer.server.webportal.mvc.model.dao.VacationDao;
import ru.kolaer.server.webportal.mvc.model.dto.holiday.FindHolidayRequest;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.holiday.HolidayEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.*;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.VacationService;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toMap;

@Service
public class VacationServiceImpl implements VacationService {
    private final int DEFAULT_VACATION_DAYS = 52;
    private final VacationDao vacationDao;
    private final EmployeeDao employeeDao;
    private final VacationConverter vacationConverter;
    private final HolidayDao holidayDao;
    private final AuthenticationService authenticationService;

    public VacationServiceImpl(VacationDao vacationDao,
                               EmployeeDao employeeDao,
                               VacationConverter vacationConverter,
                               HolidayDao holidayDao,
                               AuthenticationService authenticationService) {
        this.vacationDao = vacationDao;
        this.employeeDao = employeeDao;
        this.vacationConverter = vacationConverter;
        this.holidayDao = holidayDao;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacationDto> getVacations(FindVacationPageRequest request) {
        long countVacation = vacationDao.findCountVacation(request);

        List<VacationEntity> allVacation = countVacation == 0
                ? Collections.emptyList()
                : vacationDao.findAllVacation(request);

        return new Page<>(vacationConverter.convertToDto(allVacation),
                request.getNumber(),
                countVacation,
                request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacationPeriodDto> getVacationPeriods(FindVacationPeriodPageRequest request) {
        Long countPeriods = vacationDao.findCountPeriods(request);
        List<VacationPeriodEntity> allPeriods = vacationDao.findAllPeriods(request);

        return new Page<>(vacationConverter.convertToDtos(allPeriods),
                request.getNumber(),
                countPeriods,
                request.getPageSize());
    }

    @Override
    @Transactional
    public VacationBalanceDto getBalance(FindBalanceRequest request) {
        return vacationConverter.convertToDto(getBalanceEntity(request.getEmployeeId()));
    }

    @Override
    @Transactional(readOnly = true)
    public VacationCalculateDto vacationCalculate(VacationCalculateDaysRequest request) {
        FindHolidayRequest findHolidayRequest = new FindHolidayRequest();
        findHolidayRequest.setFromDate(request.getFrom());
        findHolidayRequest.setToDate(request.getTo());
        findHolidayRequest.setTypeHolidays(Arrays.asList(TypeDay.HOLIDAY, TypeDay.HOLIDAY_RELOCATION_FROM));

        Long countHolidays = holidayDao.findCountAll(findHolidayRequest);

        long days = DAYS.between(request.getFrom(), request.getTo()) + 1;

        VacationCalculateDto vacationCalculateDto = new VacationCalculateDto();
        vacationCalculateDto.setFrom(request.getFrom());
        vacationCalculateDto.setTo(request.getTo());
        vacationCalculateDto.setHolidayDays(countHolidays.intValue());
        vacationCalculateDto.setDays(Math.toIntExact(days - countHolidays));
        return vacationCalculateDto;
    }

    @Override
    @Transactional(readOnly = true)
    public VacationCalculateDto vacationCalculate(VacationCalculateDateRequest request) {
        LocalDate to = request.getFrom().plusDays(request.getDays());
        FindHolidayRequest findHolidayRequest = new FindHolidayRequest();
        findHolidayRequest.setFromDate(request.getFrom());
        findHolidayRequest.setToDate(to.plusMonths(1));
        findHolidayRequest.setTypeHolidays(Arrays.asList(TypeDay.HOLIDAY, TypeDay.HOLIDAY_RELOCATION_FROM));

        int days = request.getDays();
        int holidayDays = 0;

        for (HolidayEntity holiday : holidayDao.findAll(findHolidayRequest)) {
            if(holiday.getHolidayDate().isBefore(to) || holiday.getHolidayDate().isEqual(to)) {
                holidayDays += 1;
                to = to.plusDays(1);
            } else {
              break;
            }
        }

        VacationCalculateDto vacationCalculateDto = new VacationCalculateDto();
        vacationCalculateDto.setFrom(request.getFrom());
        vacationCalculateDto.setTo(to);
        vacationCalculateDto.setHolidayDays(holidayDays);
        vacationCalculateDto.setDays(days);
        return vacationCalculateDto;
    }

    @Override
    @Transactional
    public VacationDto addVacation(VacationDto request) {
        checkVacationBeforeWrite(request);

        return saveVacation(getBalanceEntity(request.getEmployeeId()), new VacationEntity(), request);
    }

    @Override
    @Transactional
    public VacationDto updateVacation(Long vacationId, VacationDto request) {
        checkVacationBeforeWrite(request);

        VacationEntity vacationEntity = vacationDao.findById(vacationId);

        int yearBeforeUpdate = vacationEntity.getVacationFrom().getYear();
        int year = LocalDate.now().getYear();

        int vacationDaysBeforeUpdate = vacationEntity.getVacationDays();

        VacationBalanceEntity balance = restoreVacationBalance(getBalanceEntity(vacationEntity.getEmployeeId()),
                year, yearBeforeUpdate, vacationDaysBeforeUpdate);

        return saveVacation(balance, vacationEntity, request);
    }

    @Override
    public void deleteVacation(Long vacationId) {
        VacationEntity vacationEntity = vacationDao.findById(vacationId);

        if (vacationEntity == null) {
            throw new NotFoundDataException("Отпуск не найден");
        }

        AccountSimpleDto account = authenticationService.getAccountSimpleByAuthentication();

        if (!account.isAccessVacationAdmin()) {
            checkVacationPeriod(vacationEntity.getVacationFrom().getYear());
            checkVacationDepartment(vacationEntity.getEmployeeId(), account.getEmployeeId());
        }

        int yearBeforeUpdate = vacationEntity.getVacationFrom().getYear();
        int year = LocalDate.now().getYear();

        int vacationDaysBeforeUpdate = vacationEntity.getVacationDays();

        VacationBalanceEntity balance = restoreVacationBalance(getBalanceEntity(vacationEntity.getEmployeeId()),
                year, yearBeforeUpdate, vacationDaysBeforeUpdate);

        vacationDao.save(balance);
    }

    private VacationDto saveVacation(VacationBalanceEntity balance, VacationEntity vacationEntity, VacationDto request) {
        VacationCalculateDaysRequest vacationCalculateDaysRequest = new VacationCalculateDaysRequest();
        vacationCalculateDaysRequest.setFrom(request.getVacationFrom());
        vacationCalculateDaysRequest.setTo(request.getVacationTo());

        VacationCalculateDto vacationCalculateDto = vacationCalculate(vacationCalculateDaysRequest);

        int year = vacationCalculateDto.getFrom().getYear();
        int currentYear = LocalDate.now().getYear();

        int vacationDays = vacationCalculateDto.getDays();

        FindBalanceRequest findBalanceRequest = new FindBalanceRequest();
        findBalanceRequest.setEmployeeId(request.getEmployeeId());

        if (currentYear < year) {
            balance.setNextYearBalance(balance.getNextYearBalance() - vacationDays);
        } else if (currentYear == year) {
            balance.setCurrentYearBalance(balance.getCurrentYearBalance() - vacationDays);
        } else {
            balance.setPrevYearBalance(balance.getPrevYearBalance() - vacationDays);
        }

        vacationDao.save(balance);

        vacationEntity.setVacationType(Optional.ofNullable(request.getVacationType()).orElse(VacationType.PAID_HOLIDAY));
        vacationEntity.setVacationFrom(vacationCalculateDto.getFrom());
        vacationEntity.setVacationTo(vacationCalculateDto.getTo());
        vacationEntity.setVacationDays(vacationCalculateDto.getDays());
        vacationEntity.setEmployeeId(request.getEmployeeId());
        if (StringUtils.hasText(request.getNote())) {
            vacationEntity.setNote(request.getNote());
        }

        return vacationConverter.convertToDto(vacationDao.save(vacationEntity));
    }

    private VacationBalanceEntity getBalanceEntity(long employeeId) {
        FindBalanceRequest request = new FindBalanceRequest();
        request.setEmployeeId(employeeId);

        VacationBalanceEntity balance = vacationDao.findBalance(request);

        if (balance == null) {
            balance = new VacationBalanceEntity();
            balance.setEmployeeId(request.getEmployeeId());
            balance.setCurrentYearBalance(DEFAULT_VACATION_DAYS);
            balance.setNextYearBalance(DEFAULT_VACATION_DAYS);

            balance = vacationDao.save(balance);
        }

        return balance;
    }

    private VacationBalanceEntity restoreVacationBalance(VacationBalanceEntity balance, int currentYear, int vacationYear, int days) {
        if (currentYear < vacationYear) {
            balance.setNextYearBalance(balance.getNextYearBalance() + days);
        } else if (currentYear == vacationYear) {
            balance.setCurrentYearBalance(balance.getCurrentYearBalance() + days);
        } else {
            balance.setPrevYearBalance(balance.getPrevYearBalance() + days);
        }

        return balance;
    }

    private void checkVacationPeriod(int year) {
        VacationPeriodEntity periodsByYear = vacationDao.findPeriodsByYear(year);

        if (periodsByYear == null || periodsByYear.getStatus() == VacationPeriodStatus.CLOSE) {
            throw new ForbiddenException("Период на " + year + " г. закрыт");
        }
    }

    private void checkVacationDepartment(long employeeVacation, long employeeAccount) {
        Map<Long, Long> employeeMap = employeeDao.findById(Arrays.asList(employeeVacation, employeeAccount))
                .stream()
                .collect(toMap(EmployeeEntity::getId, EmployeeEntity::getDepartmentId));

        long depRequestEmployee = employeeMap.get(employeeVacation);
        long depAccountEmployee = employeeMap.get(employeeAccount);

        if (depAccountEmployee != depRequestEmployee) {
            throw new ForbiddenException("У вас нет прав на внесения отпусков в это подразделение");
        }
    }

    private void checkVacationBeforeWrite(VacationDto request) {
        if (request.getEmployeeId() <= 0 ||
                request.getVacationFrom() == null ||
                request.getVacationTo() == null) {
            throw new UnexpectedRequestParams("ID сотрудника, дата начала и конца не должны быть пустыми");
        }

        if (request.getVacationFrom().isAfter(request.getVacationTo())) {
            throw new UnexpectedRequestParams("Дата начала должена быть меньше или равно дате конца");
        }

        AccountSimpleDto account = authenticationService.getAccountSimpleByAuthentication();

        if (!account.isAccessVacationAdmin()) {
            checkVacationPeriod(request.getVacationFrom().getYear());
            checkVacationDepartment(request.getEmployeeId(), account.getEmployeeId());
        }
    }
}
