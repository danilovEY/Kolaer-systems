package ru.kolaer.client.javafx.system;

import java.util.Date;

public interface UserDataBase<T> {
	T[] getAllUser();
	T[] getUsersMax(int maxCount);
	T[] getUsersByBithday(Date date);
	T[] getUsersByRengeBithday(Date dateBegin, Date dateEnd);
	T[] getUsersBithdayToday();
}
