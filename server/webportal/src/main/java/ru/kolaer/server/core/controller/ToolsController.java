package ru.kolaer.server.core.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.dto.kolaerweb.DateTimeJson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by danilovey on 25.08.2016.
 */
@RestController
@Api(tags = "Инструментарий")
public class ToolsController {

    @ApiOperation(value = "Получить серверное время")
    @GetMapping(RouterConstants.NON_SECURITY_TOOLS_SERVER_TIME)
    public DateTimeJson getTime() {
        final DateTimeJson dateTimeJson = new DateTimeJson();
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        dateTimeJson.setDate(localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        dateTimeJson.setTime(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return dateTimeJson;
    }

    @ApiOperation(value = "Получить период до времени")
    @PostMapping(RouterConstants.NON_SECURITY_TOOLS_CALCULATE_PERIOD)
    public DateTimeJson getPeriod(@ApiParam(value = "Дата-время", required = true) @RequestBody DateTimeJson dateTimeJson) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        final DateTimeJson result = new DateTimeJson();
        if(dateTimeJson != null) {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeJson.getDate() + " " + dateTimeJson.getTime(), formatter);
            LocalDateTime tempDateTime = LocalDateTime.now();

            long years = tempDateTime.until(dateTime, ChronoUnit.YEARS);
            tempDateTime = tempDateTime.plusYears(years);

            long months = tempDateTime.until(dateTime, ChronoUnit.MONTHS);
            tempDateTime = tempDateTime.plusMonths(months);

            long days = tempDateTime.until(dateTime, ChronoUnit.DAYS);
            tempDateTime = tempDateTime.plusDays(days);

            long hours = tempDateTime.until(dateTime, ChronoUnit.HOURS);
            tempDateTime = tempDateTime.plusHours(hours);

            long minutes = tempDateTime.until(dateTime, ChronoUnit.MINUTES);
            tempDateTime = tempDateTime.plusMinutes(minutes);

            long seconds = tempDateTime.until(dateTime, ChronoUnit.SECONDS);


            result.setDate(String.format("%02d-%02d-%02d", days, months, years));
            result.setTime(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        } else {
            result.setDate("31-12-9999");
            result.setTime("00:00:00");
        }
        return result;
    }

    @ApiOperation(value = "Получить период до времени")
    @GetMapping(RouterConstants.NON_SECURITY_TOOLS_CALCULATE_PERIOD)
    public DateTimeJson getPeriod(
            @ApiParam(value = "Дата", required = true) @RequestParam(PathVariableConstants.DATE) String date,
            @ApiParam(value = "Время", required = true) @RequestParam(PathVariableConstants.TIME) String time) {
        return this.getPeriod(new DateTimeJson(date, time));
    }

    @ApiOperation(value = "Получить количество после даты")
    @GetMapping(RouterConstants.NON_SECURITY_TOOLS_CALCULATE_PERIOD_DAYS)
    public long getPeriodDays(
            @ApiParam(value = "От даты", required = true) @RequestParam(PathVariableConstants.FROM_DATE) String fromDate,
            @ApiParam(value = "До даты", required = false) @RequestParam(value = PathVariableConstants.TO_DATE, required = false) String toDate) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate lDate = LocalDate.parse(fromDate,format);
        LocalDate rDate = LocalDate.now();
        if(toDate != null && !toDate.trim().isEmpty()) {
            rDate = LocalDate.parse(toDate,format);
        }

        return ChronoUnit.DAYS.between(lDate, rDate) + 1;
    }
}
