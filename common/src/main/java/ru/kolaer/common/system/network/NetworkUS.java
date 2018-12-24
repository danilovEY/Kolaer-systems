package ru.kolaer.common.system.network;

import ru.kolaer.common.system.network.kolaerweb.KolaerWebServer;

/**
 * Интерфейс для работы с сетью.
 * @author danilovey
 * @version 0.1
 */
public interface NetworkUS {
	/**Получить Kolaer-web сервер.*/
	KolaerWebServer getKolaerWebServer();
	/**Получить объект для работы со сторонними API.*/
	OtherPublicAPI getOtherPublicAPI();
}
