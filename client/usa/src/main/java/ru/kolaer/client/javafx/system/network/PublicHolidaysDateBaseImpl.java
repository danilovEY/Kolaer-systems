package ru.kolaer.client.javafx.system.network;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.mvp.model.restful.PublicHolidays;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;
import ru.kolaer.client.javafx.system.JsonConverterSingleton;

import java.time.LocalDate;
import java.util.List;

public class PublicHolidaysDateBaseImpl implements PublicHolidaysDateBase {
	private final WebResource path;
	
	public PublicHolidaysDateBaseImpl(final WebResource path) {
		this.path = path;
	}

	@Override
	public PublicHolidays[] getPublicHolidaysInThisMonth() {
		final LocalDate date = LocalDate.now();
		return this.getPublicHolidays(date.getMonthValue(), date.getYear());
	}

	@Override
	public PublicHolidays[] getPublicHolidays(final int month, final int year) {
		final List<PublicHolidays> holidays = JsonConverterSingleton.getInstance().getEntities(path.path("get").path(String.valueOf(month)).path(String.valueOf(year)), PublicHolidays.class);
		
		return listToArray(holidays);
	}

	@Override
	public PublicHolidays[] getPublicHolidaysAll() {
		final List<PublicHolidays> holidays = JsonConverterSingleton.getInstance().getEntities(path.path("get").path("all"), PublicHolidays.class);
		return listToArray(holidays);
	}
	
	private PublicHolidays[] listToArray(final List<PublicHolidays> list) {
		if(list == null || list.size() == 0) {
			return new PublicHolidays[0];
		} else {
			final PublicHolidays[] array = list.toArray(new PublicHolidays[list.size()]);
			list.clear();
			return array;
		}
	}
}