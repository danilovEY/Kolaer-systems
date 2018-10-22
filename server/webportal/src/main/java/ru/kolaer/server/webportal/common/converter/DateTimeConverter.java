package ru.kolaer.server.webportal.common.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {
    private static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter defaultDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String dateToString(LocalDate localDate) {
        return defaultDateFormatter.format(localDate);
    }
}
