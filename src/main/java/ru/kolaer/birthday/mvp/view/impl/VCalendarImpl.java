package ru.kolaer.birthday.mvp.view.impl;

import java.time.LocalDate;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import ru.kolaer.birthday.mvp.view.VCalendar;

public class VCalendarImpl implements VCalendar {
	private final BorderPane mainPane = new BorderPane();
	private final DatePicker pick = new DatePicker();
	
	
	public VCalendarImpl() {
		Platform.runLater(() -> {
			this.init();
		});
	}
	
	private void init() {
		this.pick.setShowWeekNumbers(true);
	}
	
	@Override
	public Pane getViewPane() {
		return this.mainPane;
	}

	@Override
	public void setDayCellFactory(final Callback<DatePicker, DateCell> value) {
		Platform.runLater(() -> {
			this.pick.setDayCellFactory(value);
			final DatePickerSkin skin = new DatePickerSkin(pick);
			//skin.dispose();
			this.mainPane.setCenter(skin.getPopupContent());
		});
	}

	@Override
	public LocalDate getSelectDate() {
		return this.pick.getValue();
	}

	@Override
	public void setOnAction(final EventHandler<ActionEvent> value) {
		Platform.runLater(() -> {
			this.pick.setOnAction(value);
		});
	}		
}
