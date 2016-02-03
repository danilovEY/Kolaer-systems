package ru.kolaer.client.javafx.system;

import java.util.List;

import ru.kolaer.server.dao.entities.DbBirthdayAll;

public interface UserBirthdayAllDataBase extends UserDataBase<DbBirthdayAll> {
	void insertUserList(List<DbBirthdayAll> userList);
}
