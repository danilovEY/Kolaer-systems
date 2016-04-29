package ru.kolaer.client.javafx.system;

import com.sun.jersey.api.client.WebResource;

import ru.kolaer.api.system.KolaerDataBase;
import ru.kolaer.api.system.UserBirthdayAllDataBase;
import ru.kolaer.api.system.UserDataAllDataBase;

/**
 * Реализация работы с БД через RESTful сервис.
 *
 * @author danilovey
 * @version 0.1
 */
public class KolaerDataBaseRESTful implements KolaerDataBase {
	private final UserDataAllDataBase dataAllDataBase;
	private final UserBirthdayAllDataBase userBirthdayAllDataBase;

	private final WebResource service;
	
	public KolaerDataBaseRESTful(final WebResource service) {
		this.service = service;
		this.dataAllDataBase = new UserDataAllDataBaseRESTful(service.path("dataAll"));
		this.userBirthdayAllDataBase = new UserBirthdayAllDataBaseImpl(service.path("birthdayAll"));
	}
	
	@Override
	public UserDataAllDataBase getUserDataAllDataBase() {
		return this.dataAllDataBase;
	}

	@Override
	public UserBirthdayAllDataBase getUserBirthdayAllDataBase() {
		return this.userBirthdayAllDataBase;
	}

}
