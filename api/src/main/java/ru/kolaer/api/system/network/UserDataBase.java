package ru.kolaer.api.system.network;

import java.util.Date;

public interface UserDataBase<T> {
	T[] getAllUser();
	T[] getUsersMax(int maxCount);
	T[] getUsersByBirthday(Date date);
	T[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd);
	T[] getUsersBirthdayToday();
	T[] getUsersByInitials(String initials);
	int getCountUsersBirthday(Date date);
}
