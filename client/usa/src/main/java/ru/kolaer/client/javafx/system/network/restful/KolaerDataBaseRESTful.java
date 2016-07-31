package ru.kolaer.client.javafx.system.network.restful;

import ru.kolaer.api.system.network.restful.KolaerDataBase;
import ru.kolaer.api.system.network.restful.UserBirthdayAllDataBase;
import ru.kolaer.api.system.network.restful.UserDataAllDataBase;
import ru.kolaer.client.javafx.system.network.restful.UserBirthdayAllDataBaseImpl;
import ru.kolaer.client.javafx.system.network.restful.UserDataAllDataBaseRESTful;

/**
 * Реализация работы с БД через RESTful сервис.
 *
 * @author danilovey
 * @version 0.1
 */
public class KolaerDataBaseRESTful implements KolaerDataBase {
	private final UserDataAllDataBase dataAllDataBase;
	private final UserBirthdayAllDataBase userBirthdayAllDataBase;


	public KolaerDataBaseRESTful(final String path) {
		this.dataAllDataBase = new UserDataAllDataBaseRESTful(path + "/dataAll");
		this.userBirthdayAllDataBase = new UserBirthdayAllDataBaseImpl(path + "/birthdayAll");
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
