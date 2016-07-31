package ru.kolaer.client.javafx.system.network.restful;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.system.network.restful.UserDataAllDataBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Реализация работы с таблицой через RESTful.
 *
 * @author danilovey
 * @version 1.0
 */
public class UserDataAllDataBaseRESTful implements UserDataAllDataBase {
	private final String URL_GET_USERS_MAX;
	private final String URL_GET_USERS_BY_INITIALS;
	private final String URL_GET_USERS_BIRTHDAY;
	private final String URL_GET_USERS_BIRTHDAY_TODAY;
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final RestTemplate restTemplate = new RestTemplate();
	
	public UserDataAllDataBaseRESTful(final String path) {
		this.URL_GET_USERS_MAX = path + "/get/users/max";
		this.URL_GET_USERS_BIRTHDAY = path + "/get/users/birthday";
		this.URL_GET_USERS_BIRTHDAY_TODAY = path + "/get/users/birthday/today";
		this.URL_GET_USERS_BY_INITIALS = path + "/get/users/by/initials";
	}

	@Override
	public DbDataAll[] getAllUser() {
		final DbDataAll[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX, DbDataAll[].class);
		return users;
	}

	@Override
	public DbDataAll[] getUsersMax(final int maxCount) {
		final DbDataAll[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), DbDataAll[].class);
		return users;
	}

	@Override
	public DbDataAll[] getUsersByBirthday(final Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final DbDataAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), DbDataAll[].class);
    	return users;
	}

	@Override
	public DbDataAll[] getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
    	final DbDataAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), DbDataAll[].class);
    	return users;
	}

	@Override
	public DbDataAll[] getUsersBirthdayToday() {
		final DbDataAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY_TODAY, DbDataAll[].class);
    	return users;
	}

	@Override
	public int getCountUsersBirthday(final Date date) {	
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	final Integer countUsers = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue() + "/count", Integer.class);
    	return countUsers;
	}

	@Override
	public DbDataAll[] getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final DbDataAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BY_INITIALS + "/" + initials, DbDataAll[].class);
		return users;
	}

}
