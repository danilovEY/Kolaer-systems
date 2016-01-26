package ru.kolaer.birthday.mvp.viewmodel;

import java.time.LocalDate;
import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;

public interface ObserverCalendar {
	void updateSelectedDate(LocalDate date, List<UserModel> users);
}
