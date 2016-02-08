package ru.kolaer.client.javafx.system;

/**
 * Интерфейс для работы с сетью.
 * @author danilovey
 * @version 0.1
 */
public interface NetworkUS {
	/**Получить объект для работы с БД.*/
	KolaerDataBase getKolaerDataBase();
	/**Получить статус сервера.*/
	ServerStatus getServerStatus();
}
