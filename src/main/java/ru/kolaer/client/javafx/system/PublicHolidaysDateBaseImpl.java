package ru.kolaer.client.javafx.system;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.dao.entities.PublicHolidays;
import ru.kolaer.api.system.PublicHolidaysDateBase;
import ru.kolaer.client.javafx.tools.Resources;

import java.time.LocalDate;

public class PublicHolidaysDateBaseImpl implements PublicHolidaysDateBase {

	
	@Override
	public PublicHolidays[] getPublicHolidaysInThisMonth() {
		final LocalDate date = LocalDate.now();
		return this.getPublicHolidays(date.getMonthValue(), date.getYear());
	}

	@Override
	public PublicHolidays[] getPublicHolidays(final int month, final int year) {
		final RestTemplate rest = new RestTemplate();
		
		return rest.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/other/holidays/get/" + String.valueOf(month) + "/" + String.valueOf(year), PublicHolidays[].class);
	}

	@Override
	public PublicHolidays[] getPublicHolidaysAll() {
		final RestTemplate rest = new RestTemplate();
		
		return rest.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/other/holidays/get/all", PublicHolidays[].class);
	}
}