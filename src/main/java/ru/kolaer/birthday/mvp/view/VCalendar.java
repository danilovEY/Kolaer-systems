package ru.kolaer.birthday.mvp.view;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public interface VCalendar extends View {
	void setDayCellFactory(Callback<DatePicker, DateCell> value);
}
