package ru.kolaer.server.webportal.model.servirce.impl;

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
import ru.kolaer.server.webportal.model.converter.VacationConverter;
import ru.kolaer.server.webportal.model.dao.*;
import ru.kolaer.server.webportal.model.dto.counter.FindCounterRequest;
import ru.kolaer.server.webportal.model.dto.employee.CountEmployeeInDepartmentDto;
import ru.kolaer.server.webportal.model.dto.employee.FindEmployeeByDepartment;
import ru.kolaer.server.webportal.model.dto.employee.FindEmployeePageRequest;
import ru.kolaer.server.webportal.model.dto.holiday.FindHolidayRequest;
import ru.kolaer.server.webportal.model.dto.vacation.*;
import ru.kolaer.server.webportal.model.entity.general.DepartmentEntity;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;
import ru.kolaer.server.webportal.model.entity.holiday.HolidayEntity;
import ru.kolaer.server.webportal.model.entity.other.CounterEntity;
import ru.kolaer.server.webportal.model.entity.vacation.*;
import ru.kolaer.server.webportal.model.servirce.AuthenticationService;
import ru.kolaer.server.webportal.model.servirce.VacationService;

import javax.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CounterDao counterDao;
    private final GenerateReportForVacationService generateReportForVacationService;
    private final VacationConverter vacationConverter;
    private final HolidayDao holidayDao;
    private final AuthenticationService authenticationService;

    public VacationServiceImpl(VacationDao vacationDao,
                               EmployeeDao employeeDao,
                               DepartmentDao departmentDao,
                               PostDao postDao,
                               CounterDao counterDao,
                               GenerateReportForVacationService generateReportForVacationService,
                               VacationConverter vacationConverter,
                               HolidayDao holidayDao,
                               AuthenticationService authenticationService) {
        this.vacationDao = vacationDao;
        this.employeeDao = employeeDao;
        this.departmentDao = departmentDao;
        this.postDao = postDao;
        this.counterDao = counterDao;
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

        FindCounterRequest findCounterRequest = new FindCounterRequest();
        findCounterRequest.setFrom(request.getFrom().atTime(0, 0));
        findCounterRequest.setTo(request.getTo().atTime(23, 59));
        findCounterRequest.setDisplayOnVacation(true);

        List<CounterEntity> counters = counterDao.find(findCounterRequest);

        Map<Long, Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>>> mapEntityOfYearOfMonth = new HashMap<>();

        LocalDate from = request.getFrom();

        Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> holidaysCalendarRow = new HashMap<>();
        Map<String, Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>>> counterCalendarRow = new HashMap<>();

        while (from.isBefore(request.getTo()) || from.isEqual(request.getTo())) {
            final LocalDate currentFrom = from;

            DayOfWeek dayOfWeek = currentFrom.getDayOfWeek();

            int year = currentFrom.getYear();
            int month = currentFrom.getMonthValue();
            int day = currentFrom.getDayOfMonth();

            boolean isHoliday = holidayMap.getOrDefault(year, Collections.emptyMap())
                    .containsKey(currentFrom.getDayOfYear());

            boolean isDayOff = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

            if (isHoliday || isDayOff) {
                VacationReportCalendarDayDto vacationReportCalendarDayDto = new VacationReportCalendarDayDto();
                vacationReportCalendarDayDto.setDay(String.valueOf(day));
                vacationReportCalendarDayDto.setHoliday(isHoliday);
                vacationReportCalendarDayDto.setDayOff(isDayOff);

                putDay(holidaysCalendarRow, year, month, day, vacationReportCalendarDayDto);
            } else {
                putDay(holidaysCalendarRow, year, month, day);
            }

            List<String> counterTitles = counters
                    .stream()
                    .filter(c -> {
                        LocalDate counterFrom = c.getStart().toLocalDate();
                        LocalDate counterTo = c.getEnd().toLocalDate();

                        return counterFrom.isBefore(currentFrom) && counterTo.isAfter(currentFrom) ||
                                counterFrom.isEqual(currentFrom) || counterTo.isEqual(currentFrom);
                    }).map(CounterEntity::getTitle)
                    .collect(Collectors.toList());

            for (CounterEntity counter : counters) {
                Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> countersOfYear
                        = counterCalendarRow.getOrDefault(counter.getTitle(), new HashMap<>());

                if (counterTitles.contains(counter.getTitle())) {
                    VacationReportCalendarDayDto vacationReportCalendarDayDto = new VacationReportCalendarDayDto();
                    vacationReportCalendarDayDto.setDay(String.valueOf(day));
                    vacationReportCalendarDayDto.setCounter(true);

                    putDay(countersOfYear, year, month, day, vacationReportCalendarDayDto);
                } else {
                    putDay(countersOfYear, year, month, day);
                }

                counterCalendarRow.putIfAbsent(counter.getTitle(), countersOfYear);
            }

            for (Map.Entry<Long, List<VacationEntity>> employeeVacation : employeeVacations.entrySet()) {
                boolean isVacation = employeeVacation.getValue()
                        .stream()
                        .anyMatch(v -> v.getVacationFrom().isBefore(currentFrom) &&
                                v.getVacationTo().isAfter(currentFrom) ||
                                v.getVacationFrom().isEqual(currentFrom) || v.getVacationTo().isEqual(currentFrom));

                VacationReportCalendarDayDto calendarDay = new VacationReportCalendarDayDto();
                calendarDay.setDay(String.valueOf(day));
                calendarDay.setVacation(isVacation);

                Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> yearOfMonth =
                        mapEntityOfYearOfMonth.getOrDefault(employeeVacation.getKey(), new HashMap<>());

                putDay(yearOfMonth, year, month, day, calendarDay);

                mapEntityOfYearOfMonth.putIfAbsent(employeeVacation.getKey(), yearOfMonth);
            }

            from = currentFrom.plusDays(1);
        }

        List<VacationReportCalendarEmployeeDto> calendars = new ArrayList<>();

        VacationReportCalendarEmployeeDto holidaysCalendar = new VacationReportCalendarEmployeeDto();
        holidaysCalendar.setEmployee("Праздники");
        holidaysCalendar.setYears(convertToYear(holidaysCalendarRow));

        calendars.add(holidaysCalendar);

        counterCalendarRow.keySet()
                .stream()
                .sorted(String::compareTo)
                .map(counterTitle -> {
                    VacationReportCalendarEmployeeDto counterCalendar = new VacationReportCalendarEmployeeDto();
                    counterCalendar.setEmployee(counterTitle);
                    counterCalendar.setYears(convertToYear(counterCalendarRow.get(counterTitle)));

                    return counterCalendar;
                }).forEach(calendars::add);

        for (EmployeeEntity employeeEntity : employeeDao.findById(new ArrayList<>(employeeVacations.keySet()))) {
            VacationReportCalendarEmployeeDto vacationReportCalendarEmployeeDto = new VacationReportCalendarEmployeeDto();
            vacationReportCalendarEmployeeDto.setEmployee(employeeEntity.getInitials());
            vacationReportCalendarEmployeeDto.setYears(convertToYear(mapEntityOfYearOfMonth.get(employeeEntity.getId())));

            calendars.add(vacationReportCalendarEmployeeDto);
        }

        return calendars;
    }

    private void putDay(Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> calendarRow, int year, int month, int day) {
        VacationReportCalendarDayDto calendarDay = new VacationReportCalendarDayDto();
        calendarDay.setDay(String.valueOf(day));

        putDay(calendarRow, year, month, day, calendarDay);
    }

    private List<VacationReportCalendarYearDto> convertToYear(Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> yearOfMonth) {
        if (CollectionUtils.isEmpty(yearOfMonth)) {
            return Collections.emptyList();
        }

        List<VacationReportCalendarYearDto> years = new ArrayList<>();

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

        return years;
    }

    private void putDay(Map<Integer, Map<Integer, List<VacationReportCalendarDayDto>>> calendarRow,
                        int year, int month, int day,
                        VacationReportCalendarDayDto calendarDay) {
        Map<Integer, List<VacationReportCalendarDayDto>> monthMap = calendarRow.getOrDefault(year, new HashMap<>());

        List<VacationReportCalendarDayDto> days = monthMap.getOrDefault(month, new ArrayList<>());

        days.add(calendarDay);

        calendarRow.putIfAbsent(year, monthMap);
        monthMap.putIfAbsent(month, days);
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

        VacationReportDistributeDto result = new VacationReportDistributeDto();
        result.setLineValues(new ArrayList<>());
        result.setPipeValues(new ArrayList<>());

        if (request.isGroupByDepartments()) {
            FindEmployeeByDepartment findEmployeeByDepartment = new FindEmployeeByDepartment();
            findEmployeeByDepartment.setDepartmentIds(request.getDepartmentIds());
            findEmployeeByDepartment.setEmployeeIds(request.getEmployeeIds());
            findEmployeeByDepartment.setPostIds(request.getPostIds());
            findEmployeeByDepartment.setTypeWorkIds(request.getTypeWorkIds());

            for (CountEmployeeInDepartmentDto countEmployeeInDepartmentDto : employeeDao
                    .findEmployeeByDepartmentCount(findEmployeeByDepartment)) {
                result = createReportDistributeLineValues(result,
                        countEmployeeInDepartmentDto.getDepartmentId(),
                        countEmployeeInDepartmentDto.getDepartmentName(),
                        countEmployeeInDepartmentDto.getCountEmployee(),
                        request);
            }
        } else {
            FindEmployeePageRequest findEmployeePageRequest = new FindEmployeePageRequest();
            findEmployeePageRequest.setDepartmentIds(request.getDepartmentIds());
            findEmployeePageRequest.setEmployeeIds(request.getEmployeeIds());
            findEmployeePageRequest.setPostIds(request.getPostIds());
            findEmployeePageRequest.setTypeWorkIds(request.getTypeWorkIds());

            long totalCount = employeeDao.findAllEmployeeCount(findEmployeePageRequest);

            result = createReportDistributeLineValues(result, "КолАЭР", totalCount, request);
        }

        if (request.isAddOtherData()) {
            Long maxSize = result.getLineValues()
                    .stream()
                    .flatMap(v -> v.getSeries().stream())
                    .map(VacationReportDistributeLineValueDto::getValue)
                    .max(Long::compareTo)
                    .orElse(0L);

            FindCounterRequest findCounterRequest = new FindCounterRequest();
            findCounterRequest.setFrom(request.getFrom().atTime(0, 0));
            findCounterRequest.setTo(request.getTo().atTime(23, 59));
            findCounterRequest.setDisplayOnVacation(true);

            Map<String, Long> valueForCounter = new HashMap<>();

            List<CounterEntity> counters = counterDao.find(findCounterRequest);
            counters.sort(Comparator.comparing(CounterEntity::getTitle, String::compareTo).reversed());

            for (int i = 0; i < counters.size(); i++) {
                valueForCounter.put(counters.get(i).getTitle(), maxSize + i);
            }

            Map<String, VacationReportDistributeLineDto> counterReports = new HashMap<>();

            for (Pair<LocalDate, LocalDate> fromToPair : calculateFromToDates(request.getFrom(), request.getTo(), request.getSplitType())) {
                LocalDateTime from = fromToPair.getKey().atTime(0, 0);
                LocalDateTime to = fromToPair.getValue().atTime(23, 59);

                for (CounterEntity counter : counters) {
                    if (counter.getStart().isBefore(from) && counter.getEnd().isAfter(from) ||
                            counter.getStart().isBefore(to) && counter.getEnd().isAfter(to) ||
                            counter.getStart().isEqual(from) || counter.getStart().isEqual(to) ||
                            counter.getEnd().isEqual(from) || counter.getEnd().isEqual(to) ||
                            counter.getStart().isAfter(from) && counter.getEnd().isBefore(to) ||
                            counter.getStart().isBefore(from) && counter.getEnd().isAfter(to)) {
                        VacationReportDistributeLineDto reportDistribute = counterReports
                                .getOrDefault(counter.getTitle(), new VacationReportDistributeLineDto());
                        reportDistribute.setName(counter.getTitle());

                        VacationReportDistributeLineValueDto lineValue = new VacationReportDistributeLineValueDto();
                        lineValue.setName(getPipeTitle(fromToPair.getKey(), request.getSplitType()));
                        lineValue.setValue( valueForCounter.get(counter.getTitle()));

                        reportDistribute.getSeries().add(lineValue);

                        counterReports.putIfAbsent(counter.getTitle(), reportDistribute);
                    }
                }
            }

            counterReports.keySet()
                    .stream()
                    .sorted(String::compareTo)
                    .map(counterReports::get)
                    .forEach(result.getLineValues()::add);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationReportPipeDto> generateReportTotalCount(GenerateReportTotalCountRequest request) {
        if (request.getFrom() == null || request.getTo() == null) {
            throw new UnexpectedRequestParams("Не задан период");
        }

        if (request.getFrom().isAfter(request.getTo())) {
            throw new UnexpectedRequestParams("Период не правильно задан");
        }

        List<VacationReportPipeDto> result = new ArrayList<>();

        if (request.isGroupByDepartments()) {
             Map<Long, DepartmentEntity> departmentMap = departmentDao.findById(request.getDepartmentIds())
                     .stream()
                     .collect(Collectors.toMap(DepartmentEntity::getId, Function.identity()));

             for (VacationTotalCountDepartmentEntity vacationTotalCount : vacationDao.findVacationTotalCountDepartment(request)) {
                 String title = departmentMap.get(vacationTotalCount.getDepartmentId()).getAbbreviatedName() +
                         " (" +
                         vacationTotalCount.getTotalCountEmployeeWithBalance() +
                         "/" +
                         vacationTotalCount.getTotalCountEmployeeOnDepartment() +
                         ")";

                 VacationReportPipeDto reportPipeValueDto = new VacationReportPipeDto();
                 reportPipeValueDto.setName(title);
                 reportPipeValueDto.setTotalValue(vacationTotalCount.getTotalCountEmployeeOnDepartment());

                 VacationReportPipeValueDto reportPipeValueWithVacation = new VacationReportPipeValueDto();
                 reportPipeValueWithVacation.setName("Заданы отпуска");
                 reportPipeValueWithVacation.setValue(vacationTotalCount.getTotalCountEmployeeWithBalance());

                 VacationReportPipeValueDto reportPipeValueWithOutVacation = new VacationReportPipeValueDto();
                 reportPipeValueWithOutVacation.setName("Не заданы отпуска");
                 reportPipeValueWithOutVacation.setValue(reportPipeValueDto.getTotalValue() - reportPipeValueWithVacation.getValue());

                 reportPipeValueDto.getSeries().add(reportPipeValueWithVacation);
                 reportPipeValueDto.getSeries().add(reportPipeValueWithOutVacation);

                 result.add(reportPipeValueDto);
             }
        } else {
            FindEmployeePageRequest findEmployeePageRequest = new FindEmployeePageRequest();
            findEmployeePageRequest.setDepartmentIds(request.getDepartmentIds());
            findEmployeePageRequest.setEmployeeIds(request.getEmployeeIds());
            findEmployeePageRequest.setPostIds(request.getPostIds());
            findEmployeePageRequest.setTypeWorkIds(request.getTypeWorkIds());

            long totalCount = employeeDao.findAllEmployeeCount(findEmployeePageRequest);

            long vacationTotalCount = vacationDao.findVacationTotalCount(request);

            String title = "КолАЭР (" + vacationTotalCount + "/" + totalCount + ")";

            VacationReportPipeDto reportPipeValueDto = new VacationReportPipeDto();
            reportPipeValueDto.setName(title);
            reportPipeValueDto.setTotalValue(totalCount);

            VacationReportPipeValueDto reportPipeValueWithVacation = new VacationReportPipeValueDto();
            reportPipeValueWithVacation.setName("Заданы отпуска");
            reportPipeValueWithVacation.setValue(vacationTotalCount);

            VacationReportPipeValueDto reportPipeValueWithOutVacation = new VacationReportPipeValueDto();
            reportPipeValueWithOutVacation.setName("Не заданы отпуска");
            reportPipeValueWithOutVacation.setValue(reportPipeValueDto.getTotalValue() - reportPipeValueWithVacation.getValue());

            reportPipeValueDto.getSeries().add(reportPipeValueWithVacation);
            reportPipeValueDto.getSeries().add(reportPipeValueWithOutVacation);

            result.add(reportPipeValueDto);
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

        return generateReportForVacationService.generateReportExtort(request, response);
    }

    @Transactional
    @Override
    public VacationBalanceDto updateVacationBalance(VacationBalanceDto balance) {
        VacationBalanceEntity balanceEntity = getBalanceEntity(balance.getEmployeeId());
        balanceEntity.setNextYearBalance(balance.getNextYearBalance());
        balanceEntity.setPrevYearBalance(balance.getPrevYearBalance());
        balanceEntity.setCurrentYearBalance(balance.getCurrentYearBalance());

        return vacationConverter.convertToDto(balanceEntity);
    }

    private VacationReportDistributeDto createReportDistributeLineValues(VacationReportDistributeDto vacationReportDistributeDto,
                                                                         String name,
                                                                         long totalValue,
                                                                         GenerateReportDistributeRequest request) {
        return createReportDistributeLineValues(vacationReportDistributeDto, null, name, totalValue, request);
    }

    private VacationReportDistributeDto createReportDistributeLineValues(VacationReportDistributeDto vacationReportDistributeDto,
                                                                         Long departmentId,
                                                                         String name,
                                                                         long totalValue,
                                                                         GenerateReportDistributeRequest request) {
        if (vacationReportDistributeDto.getMaxSize() < totalValue) {
            vacationReportDistributeDto.setMaxSize(totalValue);
        }

        VacationReportDistributeLineDto vacationReport = new VacationReportDistributeLineDto();
        vacationReport.setName(name);
        vacationReport.setSeries(createReportDistributeLineValues(departmentId, request));

        vacationReportDistributeDto.getLineValues().add(vacationReport);

        if (request.isAddPipesForVacation()) {
            VacationReportPipeDto pipeDto = new VacationReportPipeDto();
            pipeDto.setName(vacationReport.getName());
            pipeDto.setTotalValue(totalValue);

            for (VacationReportDistributeLineValueDto valueDto : vacationReport.getSeries()) {
                VacationReportPipeValueDto pipeValueDto = new VacationReportPipeValueDto();
                pipeValueDto.setName(valueDto.getName());
                pipeValueDto.setValue(valueDto.getValue());

                pipeDto.getSeries().add(pipeValueDto);
            }

            vacationReportDistributeDto.getPipeValues().add(pipeDto);
        }

        return vacationReportDistributeDto;
    }

    private List<VacationReportDistributeLineValueDto> createReportDistributeLineValues(Long departmentId, GenerateReportDistributeRequest request) {
        List<VacationReportDistributeLineValueDto> result = new ArrayList<>();

        for (Pair<LocalDate, LocalDate> fromToPair : calculateFromToDates(request.getFrom(), request.getTo(), request.getSplitType())) {
            long count = 0;

            if (request.isCalculateIntersections()) {
                GenerateReportDistributeRequest findVacations = new GenerateReportDistributeRequest();
                findVacations.setDepartmentIds(departmentId == null ? request.getDepartmentIds() : Collections.singleton(departmentId));
                findVacations.setEmployeeIds(request.getEmployeeIds());
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
                findVacation.setDepartmentIds(departmentId == null ? request.getDepartmentIds() : Collections.singleton(departmentId));
                findVacation.setEmployeeIds(request.getEmployeeIds());
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
