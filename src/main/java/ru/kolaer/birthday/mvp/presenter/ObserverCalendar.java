package ru.kolaer.birthday.mvp.presenter;

import java.time.LocalDate;
import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;

/**Слушатель календаря.*/
public interface ObserverCalendar {
	/**Получает оповещение о нажатии на дату со списком пользователей.*/
	void updateSelectedDate(LocalDate date, List<UserModel> users);
}
