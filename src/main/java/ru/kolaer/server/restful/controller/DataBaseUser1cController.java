package ru.kolaer.server.restful.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.DbUser1сDAO;
import ru.kolaer.server.dao.entities.DbUsers1c;

@RestController
@RequestMapping(value="/database/user1c")
public class DataBaseUser1cController {
	
	@Inject
	protected DbUser1сDAO dbUser1cDAO;
	
	@RequestMapping(value = "/get/users/max", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbUsers1c> getUsers() {
		return dbUser1cDAO.getAll();
	}
	
	@RequestMapping(value = "/get/users/max/{max}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbUsers1c> getUsers(@PathVariable int max) {
		return dbUser1cDAO.getAllMaxCount(max);
	}

	@RequestMapping(value = "/get/users/rowCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getRowCount() {
		return dbUser1cDAO.getRowCount();
	}
	
	@RequestMapping(value = "/get/users/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbUsers1c> getUsersRangeBirsday(@PathVariable String startDate, @PathVariable String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			Date startDatePasre = sdf.parse(startDate);
			Date endDatePasre = sdf.parse(endDate);
			return dbUser1cDAO.getUserRangeBirthday(startDatePasre, endDatePasre);
		} catch (ParseException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	    
		
	}
	
}
