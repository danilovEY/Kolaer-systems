package ru.kolaer.birthday.mvp.viewmodel;

import java.time.LocalDate;

public interface ObservableCalendar {
	void notifySelectedDate(LocalDate date);
	void registerObserver(ObserverCalendar observer);
	void removeObserver(ObserverCalendar observer);
	
}
