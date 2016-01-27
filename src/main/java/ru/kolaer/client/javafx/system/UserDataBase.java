package ru.kolaer.client.javafx.system;

import java.util.Date;

public interface UserDataBase<T> {
	T[] getAllUser();
	T[] getUsersMax(int maxCount);
	T[] getUsersByBirthday(Date date);
	T[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd);
	T[] getUsersBirthdayToday();
	int getCountUsersBirthday(Date date);
}
