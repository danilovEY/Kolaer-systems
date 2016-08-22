package ru.kolaer.client.javafx.system.network.restful;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.restful.DbBirthdayAll;
import ru.kolaer.api.system.network.restful.UserBirthdayAllDataBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Реализация работы с таблицой через RESTful.
 *
 * @author danilovey
 * @version 0.1
 */
public class UserBirthdayAllDataBaseImpl implements UserBirthdayAllDataBase {
	private final String URL_GET_USERS_MAX;
	private final String URL_GET_USERS_BY_INITIALS;
	private final String URL_GET_USERS_BIRTHDAY;
	private final String URL_GET_USERS_BIRTHDAY_TODAY;
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final RestTemplate restTemplate = new RestTemplate();
	
	public UserBirthdayAllDataBaseImpl(final String path) {
		this.URL_GET_USERS_MAX = path + "/get/users/max";
		this.URL_GET_USERS_BIRTHDAY = path + "/get/users/birthday";
		this.URL_GET_USERS_BIRTHDAY_TODAY = path + "/get/users/birthday/today";
		this.URL_GET_USERS_BY_INITIALS = path + "/get/users/by/initials";
	}

	@Override
	public void insertUserList(List<DbBirthdayAll> userList) {

	}

	@Override
	public DbBirthdayAll[] getUsersByBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), DbBirthdayAll[].class);
    	return users;
	}
	
	@Override
	public int getCountUsersBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));

    	final Integer countUsers = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue() + "/count", Integer.class);
    	return countUsers;
	}
	
	@Override
	public DbBirthdayAll[] getAllUser() {
		final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX, DbBirthdayAll[].class);
		return users;
	}

	@Override
	public DbBirthdayAll[] getUsersMax(final int maxCount) {
		final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), DbBirthdayAll[].class);
		return users;
	}

	@Override
	public DbBirthdayAll[] getUsersByBirthday(final Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), DbBirthdayAll[].class);
    	return users;
	}

	@Override
	public DbBirthdayAll[] getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
    	final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), DbBirthdayAll[].class);
    	return users;
	}

	@Override
	public DbBirthdayAll[] getUsersBirthdayToday() {
    	final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY_TODAY, DbBirthdayAll[].class);
    	return users;
	}

	@Override
	public DbBirthdayAll[] getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final DbBirthdayAll[] users = restTemplate.getForObject(this.URL_GET_USERS_BY_INITIALS + "/" + initials, DbBirthdayAll[].class);
		return users;
	}

	@Override
	public int getCountUsersBirthday(Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));

    	final Integer countUsers = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue() + "/count", Integer.class);
    	return countUsers;
	}
}
