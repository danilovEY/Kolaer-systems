package ru.kolaer.client.javafx.system;

import ru.kolaer.api.system.OtherPublicAPI;
import ru.kolaer.api.system.PublicHolidaysDateBase;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private final PublicHolidaysDateBase publicHolidaysDateBase = new PublicHolidaysDateBaseImpl();
	
	@Override
	public PublicHolidaysDateBase getPublicHolidaysDateBase() {
		return this.publicHolidaysDateBase;
	}

}
