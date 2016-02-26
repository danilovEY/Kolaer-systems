package ru.kolaer.server.dao.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.kolaer.server.dao.entities.PublicHolidays;
import ru.kolaer.server.restful.controller.OtherAPIController;

@Service
public class PublicHolidaysDAO {
	private final String URL = "http://kayaposoft.com/enrico/json/v1.0/?action=getPublicHolidaysForMonth";
	private final Logger LOG = LoggerFactory.getLogger(OtherAPIController.class);
	private Map<Integer, List<PublicHolidays>> holidays = new HashMap<>();

	public void initObjects() {
		holidays.clear();
		final ObjectMapper mapper = new ObjectMapper();
		try{
			for(int i = 1; i <= LocalDate.now().getMonthValue() + 1; i++){
				final PublicHolidays[] holidays = mapper.readValue(new URL(URL + "&month=" + String.valueOf(i) + "&year=2016&country=rus"), PublicHolidays[].class);
				List<PublicHolidays> list = new ArrayList<>(Arrays.asList(holidays));
				this.holidays.put(i, list);
			}
			
			
		}catch(final JsonParseException e){
			LOG.error("Невозможно распарсить строку!", e);
		}catch(final JsonMappingException e){
			LOG.error("Невозможно смапировать объект!", e);
		}catch(final MalformedURLException e){
			LOG.error("Ошибка в URL!", e);
		}catch(final IOException e){
			LOG.error("Ошибка!", e);
		}
	}

	public List<PublicHolidays> getPublicHolidaysByMonth(final int month) {
		if(month > 12 || month < 1 && !this.holidays.containsKey(month))
			return Collections.emptyList();

		return this.holidays.get(month);
	}

	public List<PublicHolidays> getAllPublicHolidays() {
		if(holidays.size() == 0)
			return Collections.emptyList();

		final List<PublicHolidays> allHoliday = new ArrayList<>();

		for(int i = 1; i <= 12; i++){
			if(this.holidays.containsKey(i))
				allHoliday.addAll(this.holidays.get(i));
		}

		return allHoliday;
	}
}
