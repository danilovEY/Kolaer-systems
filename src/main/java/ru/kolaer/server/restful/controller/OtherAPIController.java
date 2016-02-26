package ru.kolaer.server.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.entities.PublicHolidays;
import ru.kolaer.server.dao.impl.PublicHolidaysDAO;

@RestController
@RequestMapping(value = "/other")
public class OtherAPIController {
	
	@Autowired
	private PublicHolidaysDAO publicHolidaysDAO;
	
	@RequestMapping(value = "/holidays/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updatePublicHolidays() {	
		this.publicHolidaysDAO.initObjects();
	}
	
	@RequestMapping(value = "/holidays/get/{month}/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PublicHolidays[] getPublicHolidays(@PathVariable final String month, @PathVariable final String year) {	
		final List<PublicHolidays> holidays = publicHolidaysDAO.getPublicHolidaysByMonth(Integer.valueOf(month));
		return holidays.toArray(new PublicHolidays[holidays.size()]);
	}
	
	@RequestMapping(value = "/holidays/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PublicHolidays[] getPublicHolidays() {	
		final List<PublicHolidays> holidays = publicHolidaysDAO.getAllPublicHolidays();
		return holidays.toArray(new PublicHolidays[holidays.size()]);
	}
}