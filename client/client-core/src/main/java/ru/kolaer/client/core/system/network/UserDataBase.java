package ru.kolaer.client.core.system.network;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserDataBase<T> {
	ServerResponse<PageDto<T>> getAllUser();
	ServerResponse<List<T>> getUsersMax(int maxCount);
	ServerResponse<List<T>> getUsersByBirthday(LocalDate date);
	ServerResponse<List<T>> getUsersByRangeBirthday(LocalDate dateBegin, LocalDate dateEnd);
	ServerResponse<List<T>> getUsersBirthdayToday();
	ServerResponse<List<T>> getUsersByInitials(String initials);
	ServerResponse<Integer> getCountUsersBirthday(LocalDate date);
}
