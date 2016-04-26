package ru.kolaer.client.javafx.system;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.DbDataAll;
import ru.kolaer.api.system.UserDataAllDataBase;
import ru.kolaer.client.javafx.tools.Resources;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Реализация работы с таблицой через RESTful.
 *
 * @author danilovey
 * @version 0.1
 */
public class UserDataAllDataBaseRESTful implements UserDataAllDataBase {
	private final RestTemplate restTemplate = new RestTemplate();
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public UserDataAllDataBaseRESTful() {
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}
	
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

	@Override
	public DbDataAll[] getUsersByInitials(String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final DbDataAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/dataAll/get/users/by/initials/" + initials, DbDataAll[].class);
		return users;
	}

}
