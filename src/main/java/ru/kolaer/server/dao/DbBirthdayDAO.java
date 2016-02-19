package ru.kolaer.server.dao;

import java.util.Date;
import java.util.List;

public interface DbBirthdayDAO<T> extends DbDAO<T> {
	List<T> getUserRangeBirthday(Date startData, Date endData);
	List<T> getUsersByBirthday(Date date);
	List<T> getUserBirthdayToday();
	List<T> getUsersByInitials(String initials);
	int getCountUserBirthday(Date date);
}
