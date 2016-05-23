package ru.kolaer.api.system;

/**
 * БД КолАЭР.
 *
 * @author danilovey
 * @version 0.1
 */
public interface KolaerDataBase {
	/**Получить объект для работы с таблицой DataAll.*/
	UserDataAllDataBase getUserDataAllDataBase(); 
	/**Получить объект для работы с таблицой BirthdayAll.*/
	UserBirthdayAllDataBase getUserBirthdayAllDataBase();
}
