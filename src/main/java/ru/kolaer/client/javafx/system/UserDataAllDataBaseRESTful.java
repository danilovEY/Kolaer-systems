package ru.kolaer.client.javafx.system;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.client.RestTemplate;

import javafx.beans.property.SimpleStringProperty;
import ru.kolaer.client.javafx.tools.Resources;
import ru.kolaer.server.dao.entities.DbDataAll;

public class UserDataAllDataBaseRESTful implements UserDataAllDataBase{
	private final RestTemplate restTemplate = new RestTemplate();
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public DbDataAll[] getAllUser() {
		final DbDataAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/max", DbDataAll[].class);
		return users;
	}

	@Override
	public DbDataAll[] getUsersMax(int maxCount) {
		final DbDataAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/max/" + maxCount, DbDataAll[].class);
		return users;
	}

	@Override
	public DbDataAll[] getUsersByBirthday(Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
		final DbDataAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/birthday/" + property.getValue(), DbDataAll[].class);
		return users;
	}

	@Override
	public DbDataAll[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
		final DbDataAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/birthday/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), DbDataAll[].class);
		return users;
	}

	@Override
	public DbDataAll[] getUsersBirthdayToday() {
		final DbDataAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/birthday/today", DbDataAll[].class);
		return users;
	}

	@Override
	public int getCountUsersBirthday(Date date) {	
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
		final Integer countUsers = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/birthday/"+property.getValue()+"/count", Integer.class);
		return countUsers;
	}

}
