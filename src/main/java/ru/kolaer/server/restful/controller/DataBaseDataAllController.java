package ru.kolaer.server.restful.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.server.dao.DbDataAllDAO;
import ru.kolaer.server.dao.entities.DbDataAll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/database/dataAll")
public class DataBaseDataAllController {
	private final Logger LOG = LoggerFactory.getLogger(DataBaseDataAllController.class);
	
	@Autowired
	protected DbDataAllDAO dbDataAllDAO;
	
	@RequestMapping(value = "/get/users/max", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbDataAll> getUsers() {
		return dbDataAllDAO.getAll();
	}
	
	@RequestMapping(value = "/get/users/max/{max}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbDataAll> getUsers(final @PathVariable int max) {
		return dbDataAllDAO.getAllMaxCount(max);
	}

	@RequestMapping(value = "/get/users/rowCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getRowCount() {
		return dbDataAllDAO.getRowCount();
	}
	
	@RequestMapping(value = "/get/users/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbDataAll> getUsersRangeBirthday(final @PathVariable String startDate, final @PathVariable String endDate) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date startDatePasre = sdf.parse(startDate);
			final Date endDatePasre = sdf.parse(endDate);
			return dbDataAllDAO.getUserRangeBirthday(startDatePasre, endDatePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({}, {})", startDate, endDate);
			return Collections.emptyList();
		}
	}	
	
	@RequestMapping(value = "/get/users/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbDataAll> getUsersRangeBirthday() {
		return dbDataAllDAO.getUserBirthdayToday();
	}
	
	@RequestMapping(value = "/get/users/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbDataAll> getUsersRangeBirthday(final @PathVariable String date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return dbDataAllDAO.getUsersByBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}	
	
	@RequestMapping(value = "/get/users/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getCountUsersBirthday(final @PathVariable String date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return dbDataAllDAO.getCountUserBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}
	
	@RequestMapping(value = "/get/users/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbDataAll> getUsersByInitials(final @PathVariable String initials) {
			return dbDataAllDAO.getUsersByInitials(initials);
	}
}
