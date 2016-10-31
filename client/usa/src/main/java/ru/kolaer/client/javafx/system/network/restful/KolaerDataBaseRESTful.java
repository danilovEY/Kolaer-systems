package ru.kolaer.client.javafx.system.network.restful;

import ru.kolaer.api.system.network.restful.KolaerDataBase;
import ru.kolaer.api.system.network.restful.UserDataAllDataBase;

/**
 * Реализация работы с БД через RESTful сервис.
 *
 * @author danilovey
 * @version 0.1
 */
public class KolaerDataBaseRESTful implements KolaerDataBase {
	private final UserDataAllDataBase dataAllDataBase;

	public KolaerDataBaseRESTful(final String path) {
		this.dataAllDataBase = new UserDataAllDataBaseRESTful(path + "/dataAll");
	}
	
	@Override
	public UserDataAllDataBase getUserDataAllDataBase() {
		return this.dataAllDataBase;
	}

}
