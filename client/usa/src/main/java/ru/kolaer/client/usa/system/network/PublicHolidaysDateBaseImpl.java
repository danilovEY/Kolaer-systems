package ru.kolaer.client.usa.system.network;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.other.PublicHolidays;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;

import java.time.LocalDate;

public class PublicHolidaysDateBaseImpl implements PublicHolidaysDateBase {
	private final RestTemplate restTemplate;
	private final String URL_GET;
	private final String URL_GET_ALL;

	public PublicHolidaysDateBaseImpl(RestTemplate globalRestTemplate, String path) {
		this.restTemplate = globalRestTemplate;
		this.URL_GET = path + "/get";
		this.URL_GET_ALL = this.URL_GET + "/all";
	}

	@Override
	public PublicHolidays[] getPublicHolidaysInThisMonth() {
		final LocalDate date = LocalDate.now();
		return this.getPublicHolidays(date.getMonthValue(), date.getYear());
	}

	@Override
	public PublicHolidays[] getPublicHolidays(final int month, final int year) {
		final PublicHolidays[] holidays = restTemplate.getForObject(this.URL_GET + "/" + String.valueOf(month) + "/" + String.valueOf(year), PublicHolidays[].class);
		
		return holidays;
	}

	@Override
	public PublicHolidays[] getPublicHolidaysAll() {
		final PublicHolidays[] holidays = restTemplate.getForObject(this.URL_GET_ALL, PublicHolidays[].class);
		return holidays;
	}
}