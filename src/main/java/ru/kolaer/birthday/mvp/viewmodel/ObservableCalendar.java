package ru.kolaer.birthday.mvp.viewmodel;

import java.time.LocalDate;
import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;

public interface ObservableCalendar {
	void notifySelectedDate(LocalDate date);
	void registerObserver(ObserverCalendar observer);
	void removeObserver(ObserverCalendar observer);
	
}
