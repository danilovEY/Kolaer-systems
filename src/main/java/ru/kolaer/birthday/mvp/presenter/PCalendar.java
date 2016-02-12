package ru.kolaer.birthday.mvp.presenter;

import ru.kolaer.birthday.mvp.view.VCalendar;
/***/
public interface PCalendar extends ObservableCalendar {
	VCalendar getView();
	void initDayCellFactory();
	boolean isInitDayCellFactory();
}
