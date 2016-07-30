package ru.kolaer.api.system.network.restful;

import ru.kolaer.api.mvp.model.restful.DbBirthdayAll;
import ru.kolaer.api.system.network.UserDataBase;

import java.util.Date;
import java.util.List;

public interface UserBirthdayAllDataBase extends UserDataBase<DbBirthdayAll> {
	void insertUserList(List<DbBirthdayAll> userList);
	DbBirthdayAll[] getUsersByBirthday(Date date, String organization);
	int getCountUsersBirthday(Date date, String organization);
	
}
