package ru.kolaer.server.dao;

import java.util.Date;
import java.util.List;

import ru.kolaer.server.dao.entities.DbBirthdayAll;

public interface DbBirthdayAllDAO extends DbBirthdayDAO<DbBirthdayAll>, DbInsertDataDAO<DbBirthdayAll> {
	List<DbBirthdayAll> getUsersByBirthdayAndOrg(Date date, String organization);
	int getCountUserBirthdayAndOrg(Date date, String organization);
}
