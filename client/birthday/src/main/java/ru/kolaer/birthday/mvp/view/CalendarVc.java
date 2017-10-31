package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.birthday.mvp.presenter.ObservableCalendar;

public interface CalendarVc extends BaseView<CalendarVc, Parent>, ObservableCalendar {
	void setDayCellFactory(CustomCallback value);
	String getTitle();
}
