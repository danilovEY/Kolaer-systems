package ru.kolaer.api.system;

import ru.kolaer.api.dao.entities.PublicHolidays;

public interface PublicHolidaysDateBase {
	PublicHolidays[] getPublicHolidaysInThisMonth();
	PublicHolidays[] getPublicHolidays(int month, int year);
	PublicHolidays[] getPublicHolidaysAll();
}
