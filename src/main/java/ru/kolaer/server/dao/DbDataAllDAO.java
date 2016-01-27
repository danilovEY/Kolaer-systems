package ru.kolaer.server.dao;

import java.util.Date;
import java.util.List;

import ru.kolaer.server.dao.entities.DbDataAll;

public interface DbDataAllDAO extends DbDAO<DbDataAll> {
	List<DbDataAll> getUserRangeBirthday(Date startData, Date endData);
	List<DbDataAll> getUsersByBirthday(Date date);
	List<DbDataAll> getUserBirthdayToday();
	int getCountUserBirthday(Date date);
}
