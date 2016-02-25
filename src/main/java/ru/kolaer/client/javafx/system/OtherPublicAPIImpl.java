package ru.kolaer.client.javafx.system;

public class OtherPublicAPIImpl implements OtherPublicAPI {
	private final PublicHolidaysDateBase publicHolidaysDateBase = new PublicHolidaysDateBaseImpl();
	
	@Override
	public PublicHolidaysDateBase getPublicHolidaysDateBase() {
		return this.publicHolidaysDateBase;
	}

}
