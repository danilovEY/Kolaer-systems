package ru.kolaer.client.javafx.system;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.system.KolaerDataBase;
import ru.kolaer.api.system.NetworkUS;
import ru.kolaer.api.system.OtherPublicAPI;
import ru.kolaer.api.system.ServerStatus;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Реализация интерфейса для работы с сетью.
 *
 * @author danilovey
 * @version 0.1
 */
public class NetworkUSImpl implements NetworkUS {
	/**БД через RESTful.*/
	private KolaerDataBase kolaerDataBase;
	/**БД через RESTful.*/
	private OtherPublicAPI otherPublicAPI;

	private final Client client;

	private WebResource service;
	
	public NetworkUSImpl() {
        this.client = Client.create();
        this.service = client.resource("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString());
        this.kolaerDataBase = new KolaerDataBaseRESTful(service.path("database"));
        this.otherPublicAPI = new OtherPublicAPIImpl(service.path("other"));
	}
	
	@Override
	public KolaerDataBase getKolaerDataBase() {
		return this.kolaerDataBase;
	}

	@Override
	public ServerStatus getServerStatus() {
		try {
			final WebResource webRes = service.path("system").path("server").path("status");

			final String status = webRes.get(String.class);
			if(status == null)
				return ServerStatus.NOT_AVAILABLE;
			switch (status) {
				case "available": return ServerStatus.AVAILABLE;
				case "not available": return ServerStatus.NOT_AVAILABLE;
				default: return ServerStatus.UNKNOWN;
			}
		} catch(final UniformInterfaceException | ClientHandlerException ex) {
			return ServerStatus.NOT_AVAILABLE;
		}
	}

	public WebResource getService() {
		return service;
	}

	@Override
	public OtherPublicAPI getOtherPublicAPI() {
		return this.otherPublicAPI;
	}
}
