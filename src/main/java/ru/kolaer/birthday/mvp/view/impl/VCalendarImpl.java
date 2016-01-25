package ru.kolaer.birthday.mvp.view.impl;

import java.time.LocalDate;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import ru.kolaer.birthday.mvp.view.VCalendar;

public class VCalendarImpl implements VCalendar {
	private final BorderPane mainPane = new BorderPane();
	
	public VCalendarImpl() {
		this.init();
	}
	
	private void init() {
		final DatePicker pick = new DatePicker();
		pick.setShowWeekNumbers(true);
	    final Callback<DatePicker, DateCell> dayCellFactory = (datePicker) -> {
	          return new DateCell() {
	            @Override
	            public void updateItem(LocalDate item, boolean empty) {
	              super.updateItem(item, empty);
	              setStyle("-fx-background-color: #FF0000;");
	              System.out.println(item);
	            }
	          };
	      };
	    pick.setDayCellFactory(dayCellFactory);
		final DatePickerSkin skin = new DatePickerSkin(pick);
		skin.dispose();
		this.mainPane.setCenter(skin.getPopupContent());
	}
	
	@Override
	public Pane getViewPane() {
		return this.mainPane;
	}
}
