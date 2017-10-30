package ru.kolaer.birthday.mvp.presenter;

/**
 * Оповещает слушателей календаря о нажатии на дату.
 *
 * @author danilovey
 * @version 0.1
 */
public interface ObservableCalendar {
	void registerObserver(ObserverCalendar observer);
	void removeObserver(ObserverCalendar observer);
}
