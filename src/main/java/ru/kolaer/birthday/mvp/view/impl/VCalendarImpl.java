package ru.kolaer.birthday.mvp.view.impl;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.application.Platform;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
			skin.dispose();
			this.mainPane.setCenter(skin.getPopupContent());
		});
	}
		
}
