package ru.kolaer.client.javafx.system;

import com.sun.jersey.api.client.Client;
import ru.kolaer.api.system.KolaerWebServer;
import ru.kolaer.api.system.NetworkUS;
import ru.kolaer.api.system.RestfulServer;
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

	private final Client client;

	public NetworkUSImpl() {
        this.client = Client.create();

		this.restfulServer = new RestfulServerImpl(client.resource("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString()));
		this.kolaerWebServer = new KolaerWebServerImpl(client.resource("http://" + Resources.URL_TO_KOLAER_WEB.toString()));
	}

	@Override
	public RestfulServer getRestfulServer() {
		return this.restfulServer;
	}

	@Override
	public KolaerWebServer getKolaerWebServer() {
		return this.kolaerWebServer;
	}
}
