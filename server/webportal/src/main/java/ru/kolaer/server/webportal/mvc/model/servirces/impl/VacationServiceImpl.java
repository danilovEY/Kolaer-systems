package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.mvc.model.converter.VacationConverter;
import ru.kolaer.server.webportal.mvc.model.dao.HolidayDao;
import ru.kolaer.server.webportal.mvc.model.dao.VacationDao;
import ru.kolaer.server.webportal.mvc.model.dto.holiday.FindHolidayRequest;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.entities.holiday.HolidayEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationBalanceEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationPeriodEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.VacationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class VacationServiceImpl implements VacationService {
    private final int DEFAULT_VACATION_DAYS = 52;
    private final VacationDao vacationDao;
    private final VacationConverter vacationConverter;
    private final HolidayDao holidayDao;
    private final AuthenticationService authenticationService;

    public VacationServiceImpl(VacationDao vacationDao,
                               VacationConverter vacationConverter,
                               HolidayDao holidayDao,
                               AuthenticationService authenticationService) {
        this.vacationDao = vacationDao;
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
        VacationBalanceEntity balance = vacationDao.findBalance(request);

        if (balance == null) {
            balance = new VacationBalanceEntity();
            balance.setEmployeeId(request.getEmployeeId());
            balance.setCurrentYearBalance(DEFAULT_VACATION_DAYS);
            balance.setNextYearBalance(DEFAULT_VACATION_DAYS);

            balance = vacationDao.persist(balance);
        }

        return vacationConverter.convertToDto(balance);
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

        List<HolidayEntity> holidays = holidayDao.findAll(findHolidayRequest);

        System.out.println(holidays.size());

        for (HolidayEntity holiday : holidays) {
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
}
