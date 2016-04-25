package ru.kolaer.api.system;

import ru.kolaer.api.mvp.model.PublicHolidays;

public interface PublicHolidaysDateBase {
	PublicHolidays[] getPublicHolidaysInThisMonth();
	PublicHolidays[] getPublicHolidays(int month, int year);
	PublicHolidays[] getPublicHolidaysAll();
}
