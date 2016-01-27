package ru.kolaer.birthday.mvp.view;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public interface VCalendar extends View {
	void setDayCellFactory(Callback<DatePicker, DateCell> value);
	void setOnAction(EventHandler<ActionEvent> value);
	LocalDate getSelectDate();
	void setTitle(String title);
}
