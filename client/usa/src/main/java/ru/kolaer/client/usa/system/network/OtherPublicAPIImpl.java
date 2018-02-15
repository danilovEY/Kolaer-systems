package ru.kolaer.client.usa.system.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.network.HolidaysTable;
import ru.kolaer.api.system.network.KaesTable;
import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.client.usa.system.network.kolaerweb.HolidaysTableImpl;
import ru.kolaer.client.usa.tools.Resources;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private HolidaysTable holidaysTable;
	private KaesTable kaesTable;

	public OtherPublicAPIImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate) {
		this.holidaysTable = new HolidaysTableImpl(objectMapper, globalRestTemplate,
				"http://" + Resources.URL_TO_PUBLIC_SERVER + "/rest/non-security/holidays");

		this.kaesTable = new KaesTableImpl(objectMapper, globalRestTemplate, "http://iportal");
	}

	@Override
	public HolidaysTable getHolidaysTable() {
		return this.holidaysTable;
	}

	@Override
	public KaesTable getKaesTable() {
		return kaesTable;
	}

}
