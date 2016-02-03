package ru.kolaer.client.javafx.system;

public class KolaerDataBaseRESTful implements KolaerDataBase {
	private final User1cDataBase user1cDataBase = new User1cDataBaseRESTful();
	private final UserDataAllDataBase dataAllDataBase = new UserDataAllDataBaseRESTful();
	private final UserBirthdayAllDataBase userBirthdayAllDataBase = new UserBirthdayAllDataBaseImpl();
	
	@Override
	public User1cDataBase getUser1cDataBase() {
		return this.user1cDataBase;
	}

	@Override
	public UserDataAllDataBase getUserDataAllDataBase() {
		return this.dataAllDataBase;
	}

	@Override
	public UserBirthdayAllDataBase getUserBirthdayAllDataBase() {
		return this.userBirthdayAllDataBase;
	}

}
