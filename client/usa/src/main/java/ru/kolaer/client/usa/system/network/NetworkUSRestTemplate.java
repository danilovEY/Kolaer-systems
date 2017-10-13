package ru.kolaer.client.usa.system.network;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.restful.RestfulServer;
import ru.kolaer.client.usa.system.network.kolaerweb.KolaerWebServerImpl;
import ru.kolaer.client.usa.system.network.restful.RestfulServerImpl;
import ru.kolaer.client.usa.tools.Resources;

/**
 * Реализация интерфейса для работы с сетью.
 *
 * @author danilovey
 * @version 0.1
 */
public class NetworkUSRestTemplate implements NetworkUS {
	private final RestTemplate globalRestTemplate;
	private RestfulServer restfulServer;
	private KolaerWebServer kolaerWebServer;
	/**БД через RESTful.*/
	private OtherPublicAPI otherPublicAPI;

	public NetworkUSRestTemplate(ResponseErrorHandler responseErrorHandler) {
		this.globalRestTemplate = new RestTemplate();
		//Убираем лог REST'a (засоряет)
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.springframework.web.client.RestTemplate")).setLevel(Level.INFO);
		this.globalRestTemplate.setErrorHandler(responseErrorHandler);

		this.restfulServer = new RestfulServerImpl(this.globalRestTemplate, new StringBuilder("http://").append(Resources.URL_TO_KOLAER_RESTFUL));
		this.kolaerWebServer = new KolaerWebServerImpl(this.globalRestTemplate, new StringBuilder("http://").append(Resources.URL_TO_KOLAER_WEB));
		this.otherPublicAPI = new OtherPublicAPIImpl(this.globalRestTemplate);
	}

	@Override
	public RestfulServer getRestfulServer() {
		return this.restfulServer;
	}

	@Override
	public KolaerWebServer getKolaerWebServer() {
		return this.kolaerWebServer;
	}

	@Override
	public OtherPublicAPI getOtherPublicAPI() {
		return this.otherPublicAPI;
	}

}
