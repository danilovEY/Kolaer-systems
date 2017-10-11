package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.other.PublicHolidays;

import java.util.List;

public interface PublicHolidaysDateBase {
	ServerResponse<List<PublicHolidays>> getPublicHolidaysInThisMonth();
	ServerResponse<List<PublicHolidays>> getPublicHolidays(int month, int year);
	ServerResponse<List<PublicHolidays>> getPublicHolidaysAll();
}
