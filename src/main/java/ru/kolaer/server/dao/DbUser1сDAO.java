package ru.kolaer.server.dao;

import java.util.Date;
import java.util.List;

import ru.kolaer.server.dao.entities.DbUsers1c;

public interface DbUser1сDAO extends DbDAO<DbUsers1c> {
	List<DbUsers1c> getUserRangeBirthday(Date startData, Date endData);
}
