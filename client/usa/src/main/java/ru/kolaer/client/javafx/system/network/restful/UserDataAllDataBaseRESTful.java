package ru.kolaer.client.javafx.system.network.restful;

import com.sun.jersey.api.client.WebResource;
import javafx.beans.property.SimpleStringProperty;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.system.network.restful.UserDataAllDataBase;
import ru.kolaer.client.javafx.system.JsonConverterSingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Реализация работы с таблицой через RESTful.
 *
 * @author danilovey
 * @version 1.0
 */
public class UserDataAllDataBaseRESTful implements UserDataAllDataBase {
	private final WebResource path;
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public UserDataAllDataBaseRESTful(final WebResource path) {
		this.path = path;
	}

	@Override
	public DbDataAll[] getAllUser() {
		final List<DbDataAll> users = JsonConverterSingleton.getInstance().getEntities(this.path.path("get").path("users").path("max"), DbDataAll.class);
		return this.listToArray(users);
	}

	@Override
	public DbDataAll[] getUsersMax(final int maxCount) {
		final List<DbDataAll> users = JsonConverterSingleton.getInstance().getEntities(this.path.path("get").path("users").path("max").path(String.valueOf(maxCount)), DbDataAll.class);
		return this.listToArray(users);
	}

	@Override
	public DbDataAll[] getUsersByBirthday(final Date date) {
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	
    	final List<DbDataAll> users = JsonConverterSingleton.getInstance().getEntities(this.path.path("get").path("users").path("birthday").path(property.getValue()), DbDataAll.class);
    	return this.listToArray(users);
	}

	@Override
	public DbDataAll[] getUsersByRengeBirthday(final Date dateBegin, final Date dateEnd) {
		final SimpleStringProperty propertyBegin = new SimpleStringProperty();
    	final SimpleStringProperty propertyEnd = new SimpleStringProperty();
    	propertyBegin.setValue(dateFormat.format(dateBegin));
    	propertyEnd.setValue(dateFormat.format(dateEnd));
    	
    	final List<DbDataAll> users = JsonConverterSingleton.getInstance().getEntities(this.path.path("get").path("users").path("birthday").path(propertyBegin.getValue()).path(propertyEnd.getValue()), DbDataAll.class);
    	return this.listToArray(users);
	}

	@Override
	public DbDataAll[] getUsersBirthdayToday() {
    	final List<DbDataAll> users = JsonConverterSingleton.getInstance().getEntities(this.path.path("get").path("users").path("birthday").path("today"), DbDataAll.class);
    	return this.listToArray(users);
	}

	@Override
	public int getCountUsersBirthday(final Date date) {	
    	final SimpleStringProperty property = new SimpleStringProperty();
    	property.setValue(dateFormat.format(date));
    	final Integer countUsers = Integer.valueOf(this.path.path("get").path("users").path("birthday").path(property.getValue()).path("count").get(String.class));
    	return countUsers;
	}

	@Override
	public DbDataAll[] getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			throw new NullPointerException("Initials is null!");
		final List<DbDataAll> users = JsonConverterSingleton.getInstance().getEntities(this.path.path("get").path("users").path("by").path("initials").path(initials), DbDataAll.class);
		return this.listToArray(users);
	}
	
	private DbDataAll[] listToArray(final List<DbDataAll> list) {
		if(list == null || list.size() == 0) {
			return new DbDataAll[0];
		} else {
			final DbDataAll[] array = list.toArray(new DbDataAll[list.size()]);
			list.clear();
			return array;
		}
	}
}
