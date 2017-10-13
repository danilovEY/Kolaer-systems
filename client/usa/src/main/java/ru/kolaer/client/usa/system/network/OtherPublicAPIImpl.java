package ru.kolaer.client.usa.system.network;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.network.HolidaysTable;
import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;
import ru.kolaer.client.usa.system.network.kolaerweb.HolidaysTableImpl;
import ru.kolaer.client.usa.tools.Resources;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private final PublicHolidaysDateBase publicHolidaysDateBase;
	private HolidaysTable holidaysTable;

	public OtherPublicAPIImpl(RestTemplate globalRestTemplate) {
		this.publicHolidaysDateBase = new PublicHolidaysDateBaseImpl(globalRestTemplate,
				"http://" + Resources.URL_TO_KOLAER_RESTFUL + "/other" + "/holidays");

		this.holidaysTable = new HolidaysTableImpl(globalRestTemplate,
				"http://" + Resources.URL_TO_KOLAER_WEB + "/rest/non-security/holidays");
	}

	@Override
	public PublicHolidaysDateBase getPublicHolidaysDateBase() {
		return this.publicHolidaysDateBase;
	}

	@Override
	public HolidaysTable getHolidaysTable() {
		return this.holidaysTable;
	}

}
