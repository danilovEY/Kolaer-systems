package ru.kolaer.client.javafx.system;

import ru.kolaer.server.dao.entities.PublicHolidays;

public interface PublicHolidaysDateBase {
	PublicHolidays[] getPublicHolidaysInThisMonth();
	PublicHolidays[] getPublicHolidays(int month, int year);
}
