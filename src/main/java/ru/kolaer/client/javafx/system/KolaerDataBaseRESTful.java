package ru.kolaer.client.javafx.system;

/**
 * Реализация работы с БД через RESTful сервис.
 *
 * @author danilovey
 * @version 0.1
 */
public class KolaerDataBaseRESTful implements KolaerDataBase {
	private final UserDataAllDataBase dataAllDataBase = new UserDataAllDataBaseRESTful();
	private final UserBirthdayAllDataBase userBirthdayAllDataBase = new UserBirthdayAllDataBaseImpl();

	@Override
	public UserDataAllDataBase getUserDataAllDataBase() {
		return this.dataAllDataBase;
	}

	@Override
	public UserBirthdayAllDataBase getUserBirthdayAllDataBase() {
		return this.userBirthdayAllDataBase;
	}

}
