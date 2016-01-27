package ru.kolaer.client.javafx.system;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.client.RestTemplate;

import javafx.beans.property.SimpleStringProperty;
import ru.kolaer.client.javafx.tools.Resources;
import ru.kolaer.server.dao.entities.DbUsers1c;

public class User1cDataBaseRESTful implements User1cDataBase {
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public DbUsers1c[] getAllUser() {
		final DbUsers1c[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/user1c/get/users/max", DbUsers1c[].class);
		return users;
	}

	@Override
	public DbUsers1c[] getUsersMax(int maxCount) {
		final DbUsers1c[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/user1c/get/users/max/" + maxCount, DbUsers1c[].class);
		return users;
	}

	@Override
	public DbUsers1c[] getUsersByBirthday(Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	property.setValue(dateFormat.format(date));
		final DbUsers1c[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/user1c/get/users/birthday/" + property.getValue(), DbUsers1c[].class);
		return users;
	}

	@Override
	public DbUsers1c[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
		final DbUsers1c[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/user1c/get/users/birthday/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), DbUsers1c[].class);
		return users;
	}

	@Override
	public DbUsers1c[] getUsersBirthdayToday() {
		final DbUsers1c[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/user1c/get/users/birthday/today", DbUsers1c[].class);
		return users;
	}

	@Override
	public int getCountUsersBirthday(Date date) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
