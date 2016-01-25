package ru.kolaer.client.javafx.system;

public class NetworkUSImpl implements NetworkUS {
	private final KolaerDataBase kolaerDataBase = new KolaerDataBaseRESTful();
	
	@Override
	public KolaerDataBase getKolaerDataBase() {
		return this.kolaerDataBase;
	}
	
}
