package ru.kolaer.client.javafx.system.network.kolaerweb;

import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.system.network.restful.EmployeeOtherOrganizationTable;

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
public class EmployeeOtherOrganizationTableImpl implements EmployeeOtherOrganizationTable {
	private static Logger logger = LoggerFactory.getLogger(EmployeeOtherOrganizationTableImpl.class);

	private final String URL_GET_USERS_MAX;
	private final String URL_GET_USERS;
	private final String URL_GET_USERS_BY_INITIALS;
	private final String URL_GET_USERS_BIRTHDAY;
	private final String URL_GET_USERS_BIRTHDAY_TODAY;
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final RestTemplate restTemplate = new RestTemplate();
	
	public EmployeeOtherOrganizationTableImpl(final String path) {
		this.URL_GET_USERS = path + "/get/users";
		this.URL_GET_USERS_MAX = this.URL_GET_USERS + "/max";
		this.URL_GET_USERS_BIRTHDAY = this.URL_GET_USERS + "/birthday";
		this.URL_GET_USERS_BIRTHDAY_TODAY = this.URL_GET_USERS + "/birthday/today";
		this.URL_GET_USERS_BY_INITIALS = this.URL_GET_USERS + "/by/initials";
	}

	@Override
	public void insertUserList(List<EmployeeOtherOrganization> userList) {

	}

	@Override
	public EmployeeOtherOrganization[] getUsersByBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS + "/" + organization + "/birthday/" + property.getValue(), EmployeeOtherOrganization[].class);
    	return users;
	}
	
	@Override
	public int getCountUsersBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));

    	final Integer countUsers = restTemplate.getForObject(this.URL_GET_USERS + "/" + organization + "/birthday/" + property.getValue() + "/count", Integer.class);
    	return countUsers;
	}
	
	@Override
	public EmployeeOtherOrganization[] getAllUser() {
		final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX, EmployeeOtherOrganization[].class);
		return users;
	}

	@Override
	public EmployeeOtherOrganization[] getUsersMax(final int maxCount) {
		final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS_MAX + "/" + String.valueOf(maxCount), EmployeeOtherOrganization[].class);
		return users;
	}

	@Override
	public EmployeeOtherOrganization[] getUsersByBirthday(final Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + property.getValue(), EmployeeOtherOrganization[].class);
    	return users;
	}

	@Override
	public EmployeeOtherOrganization[] getUsersByRangeBirthday(final Date dateBegin, final Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
    	final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY + "/" + propertyBegin.getValue() + "/" + propertyEnd.getValue(), EmployeeOtherOrganization[].class);
    	return users;
	}

	@Override
	public EmployeeOtherOrganization[] getUsersBirthdayToday() {
    	final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS_BIRTHDAY_TODAY, EmployeeOtherOrganization[].class);
    	return users;
	}

	@Override
	public EmployeeOtherOrganization[] getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final EmployeeOtherOrganization[] users = restTemplate.getForObject(this.URL_GET_USERS_BY_INITIALS + "/" + initials, EmployeeOtherOrganization[].class);
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
