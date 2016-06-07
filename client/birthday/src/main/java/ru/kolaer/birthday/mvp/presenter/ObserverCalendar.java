package ru.kolaer.birthday.mvp.presenter;

import ru.kolaer.birthday.mvp.model.UserModel;

import java.time.LocalDate;
import java.util.List;

/**Слушатель календаря.*/
public interface ObserverCalendar {
	/**Получает оповещение о нажатии на дату со списком пользователей.*/
	void updateSelectedDate(LocalDate date, List<UserModel> users);
}
