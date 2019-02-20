package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.client.core.system.network.KaesTable;
import ru.kolaer.client.core.system.network.OtherPublicAPI;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private KaesTable kaesTable;

	OtherPublicAPIImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate) {
		this.kaesTable = new KaesTableImpl(objectMapper, globalRestTemplate, "http://iportal");
	}

	@Override
	public KaesTable getKaesTable() {
		return kaesTable;
	}

}
