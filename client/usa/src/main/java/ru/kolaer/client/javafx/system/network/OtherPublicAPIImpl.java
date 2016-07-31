package ru.kolaer.client.javafx.system.network;

import ru.kolaer.api.system.network.OtherPublicAPI;
import ru.kolaer.api.system.network.PublicHolidaysDateBase;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private final PublicHolidaysDateBase publicHolidaysDateBase;
	
	public OtherPublicAPIImpl(final StringBuilder path) {
		this.publicHolidaysDateBase = new PublicHolidaysDateBaseImpl(path.append("holidays").toString());
	}

	@Override
	public PublicHolidaysDateBase getPublicHolidaysDateBase() {
		return this.publicHolidaysDateBase;
	}

}
