package ru.kolaer.server.dao;

import ru.kolaer.server.dao.entities.DbBirthdayAll;

import java.util.Date;
import java.util.List;

public interface DbBirthdayAllDAO extends DbBirthdayDAO<DbBirthdayAll>, DbInsertDataDAO<DbBirthdayAll> {
	List<DbBirthdayAll> getUsersByBirthdayAndOrg(Date date, String organization);
	int getCountUserBirthdayAndOrg(Date date, String organization);
}
