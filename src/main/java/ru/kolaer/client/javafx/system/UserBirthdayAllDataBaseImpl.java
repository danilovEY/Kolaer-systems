package ru.kolaer.client.javafx.system;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.DbBirthdayAll;
import ru.kolaer.api.system.UserBirthdayAllDataBase;
import ru.kolaer.client.javafx.tools.Resources;

import java.nio.charset.Charset;
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
	private final RestTemplate restTemplate = new RestTemplate();
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public UserBirthdayAllDataBaseImpl() {
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}
	
	@Override
	public DbBirthdayAll[] getAllUser() {
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/max", DbBirthdayAll[].class);
		return users;
	}

	@Override
	public DbBirthdayAll[] getUsersMax(int maxCount) {
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/max/" + maxCount, DbBirthdayAll[].class);
		return users;
	}

	@Override
	public DbBirthdayAll[] getUsersByBirthday(Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/birthday/" + property.getValue(), DbBirthdayAll[].class);
		return users;
	}

	@Override
	public DbBirthdayAll[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/birthday/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), DbBirthdayAll[].class);
		return users;
	}

	@Override
	public DbBirthdayAll[] getUsersBirthdayToday() {
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/birthday/today", DbBirthdayAll[].class);
		return users;
	}

	@Override
	public int getCountUsersBirthday(Date date) {	
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
		final Integer countUsers = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/birthday/"+property.getValue()+"/count", Integer.class);
		return countUsers;
	}

	@Override
	public void insertUserList(List<DbBirthdayAll> userList) {
		this.restTemplate.postForLocation("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/set/users/list", null, userList);
	}

	@Override
	public DbBirthdayAll[] getUsersByBirthday(Date date, String organization) {
		final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/" + organization + "/birthday/" + property.getValue(), DbBirthdayAll[].class);
		return users;
	}

	@Override
	public int getCountUsersBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
		final Integer countUsers = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/" + organization + "/birthday/"+property.getValue()+"/count", Integer.class);
		return countUsers;
	}

	@Override
	public DbBirthdayAll[] getUsersByInitials(String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final DbBirthdayAll[] users = restTemplate.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/database/birthdayAll/get/users/by/initials/" + initials, DbBirthdayAll[].class);
		return users;
	}

}
