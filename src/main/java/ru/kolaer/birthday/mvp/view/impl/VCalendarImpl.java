package ru.kolaer.birthday.mvp.view.impl;

import java.time.LocalDate;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import ru.kolaer.birthday.mvp.view.VCalendar;

@SuppressWarnings("restriction")
/**
 * 
 *
 * @author danilovey
 * @version 0.1
 */
public class VCalendarImpl implements VCalendar {
	private final BorderPane mainPane = new BorderPane();
	private final DatePicker pick = new DatePicker();
	private final Label label = new Label();
	
	public VCalendarImpl() {
		Platform.runLater(() -> {
			this.init();
		});
	}
	
	private void init() {
		this.pick.setShowWeekNumbers(true);
		this.label.setAlignment(Pos.CENTER);
		this.label.setStyle("-fx-font-size: 12pt;");
		this.label.setMaxWidth(Double.MAX_VALUE);
		this.mainPane.setTop(label);
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

	@Override
	public void setTitle(final String title) {
		Platform.runLater(() -> {
			this.label.setText(title);
		});
	}		
}
