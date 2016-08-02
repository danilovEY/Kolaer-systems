package ru.kolaer.api.system.network;

import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.restful.RestfulServer;

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

	Authentication getAuthentication();
}
