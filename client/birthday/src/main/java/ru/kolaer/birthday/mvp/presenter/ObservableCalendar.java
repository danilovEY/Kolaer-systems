package ru.kolaer.birthday.mvp.presenter;

import java.time.LocalDate;

/**
 * Оповещает слушателей календаря о нажатии на дату.
 *
 * @author danilovey
 * @version 0.1
 */
public interface ObservableCalendar {
	void notifySelectedDate(LocalDate date);
	void registerObserver(ObserverCalendar observer);
	void removeObserver(ObserverCalendar observer);
	
}
