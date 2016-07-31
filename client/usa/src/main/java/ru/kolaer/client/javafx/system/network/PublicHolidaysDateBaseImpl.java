package ru.kolaer.client.javafx.system.network;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.restful.PublicHolidays;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;

import java.time.LocalDate;

public class PublicHolidaysDateBaseImpl implements PublicHolidaysDateBase {
	private final StringBuilder append;
	private final RestTemplate restTemplate = new RestTemplate();

	public PublicHolidaysDateBaseImpl(StringBuilder append) {
		this.append = append;
	}

	@Override
	public PublicHolidays[] getPublicHolidaysInThisMonth() {
		final LocalDate date = LocalDate.now();
		return this.getPublicHolidays(date.getMonthValue(), date.getYear());
	}

	@Override
	public PublicHolidays[] getPublicHolidays(final int month, final int year) {
		final PublicHolidays[] holidays = restTemplate.getForObject(append.append("get").append(String.valueOf(month)).append(String.valueOf(year)).toString(), PublicHolidays[].class);
		
		return holidays;
	}

	@Override
	public PublicHolidays[] getPublicHolidaysAll() {
		final PublicHolidays[] holidays = restTemplate.getForObject(append.append("get").append("all").toString(), PublicHolidays[].class);
		return holidays;
	}
}