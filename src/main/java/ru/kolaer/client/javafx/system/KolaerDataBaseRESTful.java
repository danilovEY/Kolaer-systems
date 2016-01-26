package ru.kolaer.client.javafx.system;

public class KolaerDataBaseRESTful implements KolaerDataBase {
	private User1cDataBase user1cDataBase = new User1cDataBaseRESTful();
	private UserDataAllDataBase dataAllDataBase = new UserDataAllDataBaseRESTful();
	
	@Override
	public User1cDataBase getUser1cDataBase() {
		return this.user1cDataBase;
	}

	@Override
	public UserDataAllDataBase getUserDataAllDataBase() {
		return this.dataAllDataBase;
	}

}
