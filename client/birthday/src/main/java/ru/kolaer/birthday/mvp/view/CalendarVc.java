package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.birthday.mvp.presenter.ObservableCalendar;
import ru.kolaer.client.core.mvp.view.BaseView;

public interface CalendarVc extends BaseView<CalendarVc, Parent>, ObservableCalendar {
	void setDayCellFactory(CustomCallback value);
	String getTitle();
}
