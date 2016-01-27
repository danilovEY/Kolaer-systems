package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.scene.control.DateCell;
import ru.kolaer.birthday.mvp.model.UserManagerModel;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VCalendar;
import ru.kolaer.birthday.mvp.view.impl.VCalendarImpl;
import ru.kolaer.birthday.mvp.viewmodel.ObserverCalendar;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class VMCalendarImpl implements VMCalendar {
	private final VCalendar view = new VCalendarImpl();
	private UserManagerModel userManager;
	private ObserverCalendar observerCalendar;
	private final UniformSystemEditorKit editorKid;
	
	public VMCalendarImpl(final UserManagerModel userManager) {
		this.userManager = userManager;	
		this.editorKid = null;
		this.init();
	}
	
	public VMCalendarImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;		
		this.init();
	}

	public VMCalendarImpl() {
		this.editorKid = null;
		this.userManager = null;	
		this.init();
	}
	
	private void init() {
		this.view.setDayCellFactory( (datePicker) -> {
	          return new DateCell() {
	            @Override
	            public void updateItem(LocalDate item, boolean empty) {
	              super.updateItem(item, empty);

	              int countUsersDataAll = editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getCountUsersBirthday(Date.from(item.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	              
	              if(countUsersDataAll != 0) {
	            	  countUsersDataAll *= 15;
	            	  this.setStyle("-fx-background-color: #"+(99-countUsersDataAll) + "" + (99-countUsersDataAll) + "FF;");
	              }
	            }
	          };
	      });

	      this.view.setOnAction(e -> {
	    	 this.notifySelectedDate(this.view.getSelectDate()); 
	      });
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

	@Override
	public void notifySelectedDate(final LocalDate date) {
		if(this.observerCalendar != null) {
			final List<UserModel> users = new ArrayList<>();
			if(this.editorKid != null) {
				final DbDataAll[] usersDataAll = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				for(DbDataAll user : usersDataAll) {
					if(!user.getCategoryUnit().equals("Рабочий")){
						final UserModel userModel = new UserModelImpl();
						userModel.setPersonNumber(user.getPersonNumber().intValue());
						userModel.setFirstName(user.getName());
						userModel.setSecondName(user.getSurname());
						userModel.setThirdName(user.getPatronymic());
						userModel.setBirthday(user.getBirthday());
						userModel.setDepartament(user.getDepartamentAbbreviated());
						userModel.setIcon(user.getVCard());
						users.add(userModel);
					}
				}
			}	
			this.observerCalendar.updateSelectedDate(date, users);
		}
	}

	@Override
	public void registerObserver(final ObserverCalendar observer) {
		this.observerCalendar = observer;
	}

	@Override
	public void removeObserver(final ObserverCalendar observer) {
		this.observerCalendar = null;
	}
}
