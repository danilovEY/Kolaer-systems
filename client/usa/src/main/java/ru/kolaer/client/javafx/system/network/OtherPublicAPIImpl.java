package ru.kolaer.client.javafx.system.network;

import ru.kolaer.api.system.network.HolidaysTable;
import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;
import ru.kolaer.client.javafx.system.network.kolaerweb.HolidaysTableImpl;
import ru.kolaer.client.javafx.tools.Resources;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private final PublicHolidaysDateBase publicHolidaysDateBase;
	private HolidaysTable holidaysTable;

	public OtherPublicAPIImpl() {
		this.publicHolidaysDateBase = new PublicHolidaysDateBaseImpl(new StringBuilder("http://").append(Resources.URL_TO_KOLAER_RESTFUL).append("/other").append("/holidays").toString());
		this.holidaysTable = new HolidaysTableImpl("http://" + Resources.URL_TO_KOLAER_WEB + "/rest/non-security/holidays");
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
