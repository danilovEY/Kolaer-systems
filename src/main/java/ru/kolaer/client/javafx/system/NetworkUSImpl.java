package ru.kolaer.client.javafx.system;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.Resources;

public class NetworkUSImpl implements NetworkUS {
	private final KolaerDataBase kolaerDataBase = new KolaerDataBaseRESTful();

	@Override
	public KolaerDataBase getKolaerDataBase() {
		return this.kolaerDataBase;
	}

	@Override
	public ServerStatus getServerStatus() {
		final RestTemplate rest = new RestTemplate();
		try {
			final String status = rest.getForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/server/status", String.class);
			switch (status) {
				case "available": return ServerStatus.AVAILABLE;
				case "not available": return ServerStatus.NOT_AVAILABLE;
				default: return ServerStatus.UNKNOW;
			}
		} catch(RestClientException ex) {
			return ServerStatus.NOT_AVAILABLE;
		}
	}
}
