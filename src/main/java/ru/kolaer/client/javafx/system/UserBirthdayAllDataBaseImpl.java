package ru.kolaer.client.javafx.system;

import javafx.beans.property.SimpleStringProperty;
import ru.kolaer.api.mvp.model.DbBirthdayAll;
import ru.kolaer.api.system.UserBirthdayAllDataBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sun.jersey.api.client.WebResource;

/**
 * Реализация работы с таблицой через RESTful.
 *
 * @author danilovey
 * @version 0.1
 */
public class UserBirthdayAllDataBaseImpl implements UserBirthdayAllDataBase {
	private final WebResource path;
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public UserBirthdayAllDataBaseImpl(final WebResource path) {
		this.path = path;
	}

	@Override
	public DbBirthdayAll[] getUsersByBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path(organization).path("birthday").path(property.getValue()), DbBirthdayAll.class);
    	return this.listToArray(users);
	}
	
	@Override
	public int getCountUsersBirthday(Date date, String organization) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));

    	final Integer countUsers = JsonConverterSinleton.getInstance().getEntity(this.path.path("get").path("users").path(organization).path("birthday").path(property.getValue()).path("count"), Integer.class);
    	return countUsers;
	}
	
	@Override
	public DbBirthdayAll[] getAllUser() {
		final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path("max"), DbBirthdayAll.class);
		return this.listToArray(users);
	}

	@Override
	public DbBirthdayAll[] getUsersMax(final int maxCount) {
		final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path("max").path(String.valueOf(maxCount)), DbBirthdayAll.class);
		return this.listToArray(users);
	}

	@Override
	public DbBirthdayAll[] getUsersByBirthday(final Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path("birthday").path(property.getValue()), DbBirthdayAll.class);
    	return this.listToArray(users);
	}

	@Override
	public DbBirthdayAll[] getUsersByRengeBirthday(final Date dateBegin, final Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
    	final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path("birthday").path(propertyBegin.getValue()).path(propertyEnd.getValue()), DbBirthdayAll.class);
    	return this.listToArray(users);
	}

	@Override
	public DbBirthdayAll[] getUsersBirthdayToday() {
    	final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path("birthday").path("today"), DbBirthdayAll.class);
    	return this.listToArray(users);
	}

	@Override
	public DbBirthdayAll[] getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final List<DbBirthdayAll> users = JsonConverterSinleton.getInstance().getEntitys(this.path.path("get").path("users").path("by").path("initials").path(initials), DbBirthdayAll.class);
		return this.listToArray(users);
	}
	
	private DbBirthdayAll[] listToArray(final List<DbBirthdayAll> list) {
		if(list == null || list.size() == 0) {
			return new DbBirthdayAll[0];
		} else {
			final DbBirthdayAll[] array = list.toArray(new DbBirthdayAll[list.size()]);
			list.clear();
			return array;
		}
	}

	@Override
	public int getCountUsersBirthday(Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));

    	final Integer countUsers = JsonConverterSinleton.getInstance().getEntity(this.path.path("get").path("users").path("birthday").path(property.getValue()).path("count"), Integer.class);
    	return countUsers;
	}

	@Override
	public void insertUserList(List<DbBirthdayAll> userList) {
		
	}
}
