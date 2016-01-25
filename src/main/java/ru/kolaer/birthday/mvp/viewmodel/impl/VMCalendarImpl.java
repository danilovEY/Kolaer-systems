package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import ru.kolaer.birthday.mvp.model.UserManagerModel;
import ru.kolaer.birthday.mvp.view.VCalendar;
import ru.kolaer.birthday.mvp.view.impl.VCalendarImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;

public class VMCalendarImpl implements VMCalendar {
	private final VCalendar view = new VCalendarImpl();
	private UserManagerModel userManager;
	
	public VMCalendarImpl(final UserManagerModel userManager) {
		this.userManager = userManager;		
		this.init();
	}

	public VMCalendarImpl() {
		this(null);
	}
	
	private void init() {
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
	      
	      this.view.setDayCellFactory(dayCellFactory);
	}
	
	@Override
	public VCalendar getView() {
		return this.view;
	}

	@Override
	public void setModel(final UserManagerModel model) {
		this.userManager = model;
	}

	@Override
	public UserManagerModel getModel() {
		return this.userManager;
	}
}
