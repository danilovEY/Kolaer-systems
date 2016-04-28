package ru.kolaer.client.javafx.system;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
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
	private final KolaerDataBase kolaerDataBase = new KolaerDataBaseRESTful();
	/**БД через RESTful.*/
	private final OtherPublicAPI otherPublicAPI = new OtherPublicAPIImpl();

	@Override
	public KolaerDataBase getKolaerDataBase() {
		return this.kolaerDataBase;
	}

	@Override
	public ServerStatus getServerStatus() {
		final RestTemplate rest = new RestTemplate();
		try {
			final String status = rest.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/server/status", String.class);
			if(status == null)
				return ServerStatus.NOT_AVAILABLE;
			switch (status) {
				case "available": return ServerStatus.AVAILABLE;
				case "not available": return ServerStatus.NOT_AVAILABLE;
				default: return ServerStatus.UNKNOW;
			}
		} catch(final RestClientException ex) {
			return ServerStatus.NOT_AVAILABLE;
		}
	}

	@Override
	public OtherPublicAPI getOtherPublicAPI() {
		return this.otherPublicAPI;
	}
}
