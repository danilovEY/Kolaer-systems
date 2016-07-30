package ru.kolaer.client.javafx.system.network;

import com.sun.jersey.api.client.Client;
import ru.kolaer.api.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.network.restful.RestfulServer;
import ru.kolaer.client.javafx.system.network.kolaerweb.KolaerWebServerImpl;
import ru.kolaer.client.javafx.system.network.restful.RestfulServerImpl;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Реализация интерфейса для работы с сетью.
 *
 * @author danilovey
 * @version 0.1
 */
public class NetworkUSImpl implements NetworkUS {
	private RestfulServer restfulServer;
	private KolaerWebServer kolaerWebServer;
	/**БД через RESTful.*/
	private OtherPublicAPI otherPublicAPI;

	private final Client client;

	public NetworkUSImpl() {
        this.client = Client.create();

		this.restfulServer = new RestfulServerImpl(client.resource("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString()));
		this.kolaerWebServer = new KolaerWebServerImpl(client.resource("http://" + Resources.URL_TO_KOLAER_WEB.toString()));
		this.otherPublicAPI = new OtherPublicAPIImpl(client.resource("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString()).path("other"));
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