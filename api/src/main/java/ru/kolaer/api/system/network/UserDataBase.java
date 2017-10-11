package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;

import java.util.Date;
import java.util.List;

public interface UserDataBase<T> {
	ServerResponse<List<T>> getAllUser();
	ServerResponse<List<T>> getUsersMax(int maxCount);
	ServerResponse<List<T>> getUsersByBirthday(Date date);
	ServerResponse<List<T>> getUsersByRangeBirthday(Date dateBegin, Date dateEnd);
	ServerResponse<List<T>> getUsersBirthdayToday();
	ServerResponse<List<T>> getUsersByInitials(String initials);
	ServerResponse<Integer> getCountUsersBirthday(Date date);
}
