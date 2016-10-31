package ru.kolaer.client.javafx.system.network.restful;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.restful.EmployeeOtherOrganizationBase;
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
	public void insertUserList(List<EmployeeOtherOrganizationBase> userList) {

	}

	@Override
	public EmployeeOtherOrganizationBase[] getUsersByBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), EmployeeOtherOrganizationBase[].class);
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
	public EmployeeOtherOrganizationBase[] getAllUser() {
		final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX, EmployeeOtherOrganizationBase[].class);
		return users;
	}

	@Override
	public EmployeeOtherOrganizationBase[] getUsersMax(final int maxCount) {
		final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), EmployeeOtherOrganizationBase[].class);
		return users;
	}

	@Override
	public EmployeeOtherOrganizationBase[] getUsersByBirthday(final Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), EmployeeOtherOrganizationBase[].class);
    	return users;
	}

	@Override
	public EmployeeOtherOrganizationBase[] getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
    	final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), EmployeeOtherOrganizationBase[].class);
    	return users;
	}

	@Override
	public EmployeeOtherOrganizationBase[] getUsersBirthdayToday() {
    	final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY_TODAY, EmployeeOtherOrganizationBase[].class);
    	return users;
	}

	@Override
	public EmployeeOtherOrganizationBase[] getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final EmployeeOtherOrganizationBase[] users = restTemplate.getForObject(this.URL_GET_USERS_BY_INITIALS + "/" + initials, EmployeeOtherOrganizationBase[].class);
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
