package ru.kolaer.api.system;

/**
 * Интерфейс для работы с сетью.
 * @author danilovey
 * @version 0.1
 */
public interface NetworkUS {
	/**Получить RESTful сервер.*/
	RestfulServer getRestfulServer();
	/**Получить Kolaer-web сервер.*/
	KolaerWebServer getKolaerWebServer();

	/**Получить объект для работы со сторонними API.*/
	OtherPublicAPI getOtherPublicAPI();
}
