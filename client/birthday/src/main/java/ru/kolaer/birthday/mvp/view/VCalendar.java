package ru.kolaer.birthday.mvp.view;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

/**
 * View - календаря.
 *
 * @author danilovey
 * @version 0.1
 */
public interface VCalendar extends View {
	/**Задать рендер клетки с днями на календаре.*/
	void setDayCellFactory(Callback<DatePicker, DateCell> value);
	/**Задать нажатие на клетку с днем.*/
	void setOnAction(EventHandler<ActionEvent> value);
	/**Получить дату выбранную пользователем.*/
	LocalDate getSelectDate();
	void setTitle(String title);
	String getTitle();
}
