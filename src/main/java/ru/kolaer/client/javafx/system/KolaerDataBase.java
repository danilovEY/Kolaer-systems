package ru.kolaer.client.javafx.system;

/**
 * БД КолАЭР.
 *
 * @author danilovey
 * @version 0.1
 */
public interface KolaerDataBase {
	/**Получить объект для работы с таблицой User1C.*/
	User1cDataBase getUser1cDataBase();
	/**Получить объект для работы с таблицой DataAll.*/
	UserDataAllDataBase getUserDataAllDataBase(); 
	/**Получить объект для работы с таблицой BirthdayAll.*/
	UserBirthdayAllDataBase getUserBirthdayAllDataBase();
}
