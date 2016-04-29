package ru.kolaer.client.javafx.system;

import com.sun.jersey.api.client.WebResource;

import ru.kolaer.api.system.OtherPublicAPI;
import ru.kolaer.api.system.PublicHolidaysDateBase;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private final PublicHolidaysDateBase publicHolidaysDateBase;
	
	public OtherPublicAPIImpl(final WebResource path) {
		this.publicHolidaysDateBase = new PublicHolidaysDateBaseImpl(path.path("holidays"));
	}

	@Override
	public PublicHolidaysDateBase getPublicHolidaysDateBase() {
		return this.publicHolidaysDateBase;
	}

}
