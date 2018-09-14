package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import javafx.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.VacationConverter;
import ru.kolaer.server.webportal.mvc.model.dao.*;
import ru.kolaer.server.webportal.mvc.model.dto.employee.CountEmployeeInDepartmentDto;
import ru.kolaer.server.webportal.mvc.model.dto.employee.FindEmployeeByDepartment;
import ru.kolaer.server.webportal.mvc.model.dto.holiday.FindHolidayRequest;
import ru.kolaer.server.webportal.mvc.model.dto.vacation.*;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.holiday.HolidayEntity;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.*;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.VacationService;

import javax.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toMap;

@Service
public class VacationServiceImpl implements VacationService {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
    private final int DEFAULT_VACATION_DAYS = 52;
    private final VacationDao vacationDao;
    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;
    private final PostDao postDao;
    private final GenerateReportForVacationService generateReportForVacationService;
    private final VacationConverter vacationConverter;
    private final HolidayDao holidayDao;
    private final AuthenticationService authenticationService;

    public VacationServiceImpl(VacationDao vacationDao,
                               EmployeeDao employeeDao,
                               DepartmentDao departmentDao,
                               PostDao postDao,
                               GenerateReportForVacationService generateReportForVacationService,
                               VacationConverter vacationConverter,
                               HolidayDao holidayDao,
                               AuthenticationService authenticationService) {
        this.vacationDao = vacationDao;
        this.employeeDao = employeeDao;
        this.departmentDao = departmentDao;
        this.postDao = postDao;
        this.generateReportForVacationService = generateReportForVacationService;
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
        AccountSimpleDto account = authenticationService.getAccountSimpleByAuthentication();

        if(!account.isAccessVacationAdmin() && !account.isAccessOk() && !account.isAccessOit() &&
                (account.getEmployeeId() != null && request.getEmployeeId() != account.getEmployeeId())) {
            checkVacationDepartment(request.getEmployeeId(), account.getEmployeeId());
        }

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
        LocalDate to = request.getFrom().plusDays(request.getDays() - 1);
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
    @Transactional
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

        vacationDao.delete(vacationEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationReportCalendarEmployeeDto> generateReportCalendar(GenerateReportCalendarRequest request) {
        if (request.getFrom() == null || request.getTo() == null) {
            throw new UnexpectedRequestParams("Диапозон дат не задан");
        }

        if (request.getFrom().isAfter(request.getTo())) {
            throw new UnexpectedRequestParams("Начало даты должно быть меньше конца даты");
        }

        Map<Long, List<VacationEntity>> employeeVacations = vacationDao.findAll(request)
                .stream()
                .collect(Collectors.groupingBy(VacationEntity::getEmployeeId, Collectors.toList()));

        if (employeeVacations.isEmpty()) {
            return Collections.emptyList();
        }

        FindHolidayRequest findHolidayRequest = new FindHolidayRequest();
        findHolidayRequest.setFromDate(request.getFrom());
        findHolidayRequest.setToDate(request.getTo());
        findHolidayRequest.setTypeHolidays(Arrays.asList(TypeDay.HOLIDAY, TypeDay.HOLIDAY_RELOCATION_FROM));

        Map<Integer, Map<Integer, HolidayEntity>> holidayMap = holidayDao.findAll(findHolidayRequest)
                .stream()
                .collect(Collectors.groupingBy(h -> h.getHolidayDate().getYear(),
                        Collectors.toMap(h -> h.getHolidayDate().getDayOfYear(), Function.identity())));

        Map<Long, Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>>> mapEntityOfYearOfMonth = new HashMap<>();

        LocalDate from = request.getFrom();

        while (from.isBefore(request.getTo()) || from.isEqual(request.getTo())) {
            int year = from.getYear();
            int month = from.getMonthValue();
            int day = from.getDayOfMonth();
            boolean isHoliday = false;
            boolean isDayOff = false;

            DayOfWeek dayOfWeek = from.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                isDayOff = true;
            }

            Map<Integer, HolidayEntity> integerHolidayEntityMap = holidayMap.get(year);
            if (integerHolidayEntityMap != null) {
                if (integerHolidayEntityMap.containsKey(from.getDayOfYear())) {
                    isHoliday = true;
                }
            }

            for (Map.Entry<Long, List<VacationEntity>> employeeVacation : employeeVacations.entrySet()) {
                VacationReportCalendarDayDto vacationReportCalendarDayDto = new VacationReportCalendarDayDto();
                vacationReportCalendarDayDto.setDay(String.valueOf(day));
                vacationReportCalendarDayDto.setHoliday(isHoliday);
                vacationReportCalendarDayDto.setDayOff(isDayOff);

                for (VacationEntity vacation : employeeVacation.getValue()) {
                    if (vacation.getVacationFrom().isBefore(from) && vacation.getVacationTo().isAfter(from) ||
                            vacation.getVacationFrom().isEqual(from) || vacation.getVacationTo().isEqual(from)) {
                        vacationReportCalendarDayDto.setVacation(true);
                        break;
                    }
                }

                Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> yearOfMonth =
                        mapEntityOfYearOfMonth.getOrDefault(employeeVacation.getKey(), new HashMap<>());

                Map<Integer, List<VacationReportCalendarDayDto>> monthMap = yearOfMonth.getOrDefault(year, new HashMap<>());

                List<VacationReportCalendarDayDto> days = monthMap.getOrDefault(month, new ArrayList<>());

                days.add(vacationReportCalendarDayDto);

                mapEntityOfYearOfMonth.putIfAbsent(employeeVacation.getKey(), yearOfMonth);
                yearOfMonth.putIfAbsent(year, monthMap);
                monthMap.putIfAbsent(month, days);
            }

            from = from.plusDays(1);
        }

        List<VacationReportCalendarEmployeeDto> employeesVacations = new ArrayList<>();

        for (EmployeeEntity employeeEntity : employeeDao.findById(new ArrayList<>(employeeVacations.keySet()))) {
            List<VacationReportCalendarYearDto> years = new ArrayList<>();

            Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> yearOfMonth = mapEntityOfYearOfMonth.get(employeeEntity.getId());

            for (Integer year : yearOfMonth.keySet()) {
                List<VacationReportCalendarMonthDto> months = new ArrayList<>();

                Map<Integer, List<VacationReportCalendarDayDto>> monthOfDays = yearOfMonth.get(year);
                for (Integer month : monthOfDays.keySet()) {
                    VacationReportCalendarMonthDto vacationReportCalendarMonthDto = new VacationReportCalendarMonthDto();
                    vacationReportCalendarMonthDto.setMonth(Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru")));
                    vacationReportCalendarMonthDto.setDays(monthOfDays.get(month));

                    months.add(vacationReportCalendarMonthDto);
                }

                VacationReportCalendarYearDto vacationReportCalendarYearDto = new VacationReportCalendarYearDto();
                vacationReportCalendarYearDto.setYear(year.toString());
                vacationReportCalendarYearDto.setMonths(months);

                years.add(vacationReportCalendarYearDto);
            }


            VacationReportCalendarEmployeeDto vacationReportCalendarEmployeeDto = new VacationReportCalendarEmployeeDto();
            vacationReportCalendarEmployeeDto.setEmployee(employeeEntity.getInitials());
            vacationReportCalendarEmployeeDto.setYears(years);

            employeesVacations.add(vacationReportCalendarEmployeeDto);
        }

        return employeesVacations;
    }

    @Override
    @Transactional(readOnly = true)
    public VacationReportDistributeDto generateReportDistribute(GenerateReportDistributeRequest request) {
        if (request.getFrom() == null || request.getTo() == null) {
            throw new UnexpectedRequestParams("Не задан период");
        }

        if (request.getFrom().isAfter(request.getTo())) {
            throw new UnexpectedRequestParams("Период не правильно задан");
        }

        if (CollectionUtils.isEmpty(request.getDepartmentIds()) && !request.isAllDepartment()) {
            throw new UnexpectedRequestParams("Не задано подразделение");
        }

        VacationReportDistributeDto result = new VacationReportDistributeDto();
        result.setLineValues(new ArrayList<>());
        result.setPipeValues(new ArrayList<>());

        if (request.isAllDepartment()) {
            return createReportDistributeLineValues(result,
                    null, "КолАЭР", employeeDao.findAllCount(), request);
        } else {
            FindEmployeeByDepartment findEmployeeByDepartment = new FindEmployeeByDepartment();
            findEmployeeByDepartment.setDepartmentIds(request.getDepartmentIds());

            for (CountEmployeeInDepartmentDto countEmployeeInDepartmentDto : employeeDao
                    .findEmployeeByDepartmentCount(findEmployeeByDepartment)) {
                result = createReportDistributeLineValues(result,
                        countEmployeeInDepartmentDto.getDepartmentId(),
                        countEmployeeInDepartmentDto.getDepartmentName(),
                        countEmployeeInDepartmentDto.getCountEmployee(),
                        request);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public ResponseEntity generateReportExport(GenerateReportExportRequest request, HttpServletResponse response) {
        if (request.getFrom() == null || request.getTo() == null) {
            throw new UnexpectedRequestParams("Не задан период");
        }

        if (request.getFrom().isAfter(request.getTo())) {
            throw new UnexpectedRequestParams("Период не правильно задан");
        }

        if (request.getDepartmentId() <= 0) {
            throw new UnexpectedRequestParams("Не задано подразделение");
        }

        return generateReportForVacationService.generateReportExtort(request, response);
    }

    private VacationReportDistributeDto createReportDistributeLineValues(VacationReportDistributeDto vacationReportDistributeDto,
                                                                         Long depId,
                                                                         String name,
                                                                         long totalValue,
                                                                         GenerateReportDistributeRequest request) {
        if (vacationReportDistributeDto.getMaxSize() < totalValue) {
            vacationReportDistributeDto.setMaxSize(totalValue);
        }

        VacationReportDistributeLineDto vacationReport = new VacationReportDistributeLineDto();
        vacationReport.setName(name);
        vacationReport.setSeries(createReportDistributeLineValues(depId, request));

        vacationReportDistributeDto.getLineValues().add(vacationReport);

        if (request.isAddPipesForVacation()) {
            VacationReportDistributePipeDto pipeDto = new VacationReportDistributePipeDto();
            pipeDto.setName(vacationReport.getName());
            pipeDto.setTotalValue(totalValue);
            pipeDto.setValues(new ArrayList<>());

            for (VacationReportDistributeLineValueDto valueDto : vacationReport.getSeries()) {
                VacationReportDistributePipeValueDto pipeValueDto = new VacationReportDistributePipeValueDto();
                pipeValueDto.setName(valueDto.getName());
                pipeValueDto.setValue(valueDto.getValue());

                pipeDto.getValues().add(pipeValueDto);
            }

            vacationReportDistributeDto.getPipeValues().add(pipeDto);
        }

        return vacationReportDistributeDto;
    }

    private List<VacationReportDistributeLineValueDto> createReportDistributeLineValues(Long depId, GenerateReportDistributeRequest request) {
        List<VacationReportDistributeLineValueDto> result = new ArrayList<>();

        for (Pair<LocalDate, LocalDate> fromToPair : calculateFromToDates(request.getFrom(), request.getTo(), request.getSplitType())) {
            long count = 0;

            if (request.isCalculateIntersections()) {
                GenerateReportDistributeRequest findVacations = new GenerateReportDistributeRequest();
                findVacations.setDepartmentIds(depId != null ? Collections.singletonList(depId) : Collections.emptyList());
                findVacations.setPostIds(request.getPostIds());
                findVacations.setTypeWorkIds(request.getTypeWorkIds());
                findVacations.setFrom(fromToPair.getKey());
                findVacations.setTo(fromToPair.getValue());

                count = calculateVacation(request.getSplitType(),
                        fromToPair.getKey(),
                        fromToPair.getValue(),
                        vacationDao.findAll(findVacations));
            } else {
                FindVacationByDepartmentRequest findVacation = new FindVacationByDepartmentRequest();
                findVacation.setDepartmentIds(depId != null ? Collections.singletonList(depId) : Collections.emptyList());
                findVacation.setPostIds(request.getPostIds());
                findVacation.setTypeWorkIds(request.getTypeWorkIds());
                findVacation.setFrom(fromToPair.getKey());
                findVacation.setTo(fromToPair.getValue());

                count = vacationDao.findCountVacation(findVacation);
            }

            VacationReportDistributeLineValueDto lineValue = new VacationReportDistributeLineValueDto();
            lineValue.setName(getPipeTitle(fromToPair.getKey(), request.getSplitType()));
            lineValue.setValue(count);

            result.add(lineValue);
        }

        return result;
    }

    private long calculateVacation(GenerateReportDistributeSplitType splitType,
                                   LocalDate from, LocalDate to,
                                   List<VacationEntity> vacations) {
        switch (splitType) {
            case MONTHS:
            default: return calculateVacationByMonth(vacations, from, to);
        }
    }

    private long calculateVacationByMonth(List<VacationEntity> vacations, LocalDate from, LocalDate to) {
        int maxCount = 0;

        int maxIndex = to.getDayOfYear() - from.getDayOfYear();
        LocalDate day = from;

        for (int i = 0; i < maxIndex; i++) {
            day = day.plusDays(i);

            int dayCount = 0;

            for (VacationEntity vacation : vacations) {
                if (day.equals(vacation.getVacationFrom()) || day.equals(vacation.getVacationTo()) ||
                        (day.isAfter(vacation.getVacationFrom()) && day.isBefore(vacation.getVacationTo()))) {
                    dayCount += 1;
                }
            }

            if (maxCount < dayCount) {
                maxCount = dayCount;
            }
        }

        return maxCount;
    }

    private List<VacationReportDistributeLineValueDto> createReportDistributeLineValues(GenerateReportDistributeRequest request) {
        return createReportDistributeLineValues(null, request);
    }


    private String getPipeTitle(LocalDate from, GenerateReportDistributeSplitType splitType) {
        if (splitType == GenerateReportDistributeSplitType.MONTHS) {
            return from.getMonth().getDisplayName(TextStyle.FULL_STANDALONE , new Locale("ru", "RU")) ;
        }

        return dateTimeFormatter.format(from);
    }

    private List<Pair<LocalDate, LocalDate>> calculateFromToDates(LocalDate fromDate, LocalDate toDate, GenerateReportDistributeSplitType splitType) {
        List<Pair<LocalDate, LocalDate>> dates = new ArrayList<>();

        if (splitType == GenerateReportDistributeSplitType.MONTHS) {
            int countMonth = toDate.getMonthValue() - fromDate.getMonthValue();

            LocalDate from = fromDate;

            for (int i = 0; i <= countMonth; i++) {
                LocalDate to;
                if (from.getDayOfMonth() != 1) {
                    to = LocalDate.of(from.getYear(), from.getMonthValue(), from.getDayOfMonth());
                } else {
                    to = from.plusMonths(1).minusDays(1);
                    if (to.isAfter(toDate)) {
                        to = toDate;
                    }
                }

                dates.add(new Pair<>(from, to));

                from = from.plusMonths(1);

                if (from.getDayOfMonth() != 1) {
                    from = LocalDate.of(from.getYear(), from.getMonthValue(), 1);
                }
            }

        }

        return dates;
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
