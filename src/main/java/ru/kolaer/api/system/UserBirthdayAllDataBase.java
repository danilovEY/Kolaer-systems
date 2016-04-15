package ru.kolaer.api.system;

import java.util.Date;
import java.util.List;

import ru.kolaer.api.dao.entities.DbBirthdayAll;

public interface UserBirthdayAllDataBase extends UserDataBase<DbBirthdayAll> {
	void insertUserList(List<DbBirthdayAll> userList);
	DbBirthdayAll[] getUsersByBirthday(Date date, String organization);
	int getCountUsersBirthday(Date date, String organization);
	
}
