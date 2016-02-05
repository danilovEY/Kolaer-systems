package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.kolaer.birthday.mvp.model.UserManagerModel;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VCalendar;
import ru.kolaer.birthday.mvp.view.impl.VCalendarImpl;
import ru.kolaer.birthday.mvp.viewmodel.ObserverCalendar;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;
import ru.kolaer.client.javafx.system.DefaultProgressBar;
import ru.kolaer.client.javafx.system.ProgressBarObservable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class VMCalendarKAER implements VMCalendar {
	
	private final String ORGANIZATION = "КолАтомэнергоремонт";
	private final VCalendar view = new VCalendarImpl();
	private UserManagerModel userManager;
	private ObserverCalendar observerCalendar;
	private final UniformSystemEditorKit editorKid;

	public VMCalendarKAER(final UserManagerModel userManager) {
		this.userManager = userManager;
		this.editorKid = null;
		this.init();
	}

	public VMCalendarKAER(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.init();
	}

	public VMCalendarKAER() {
		this.editorKid = null;
		this.userManager = null;
		this.init();
	}

	private void init() {
		this.view.setDayCellFactory(new CustomCallback(editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase()));	
		this.view.setTitle(ORGANIZATION);
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
		if (this.observerCalendar != null) {
			final List<UserModel> users = new ArrayList<>();
			if (this.editorKid != null) {
				final ProgressBarObservable obs = new DefaultProgressBar();
				this.editorKid.getUISystemUS().getStatusBar().addProgressBar(obs);
				final DbDataAll[] usersDataAll = this.editorKid.getUSNetwork().getKolaerDataBase()
						.getUserDataAllDataBase()
						.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				obs.setValue(-1);
				if(usersDataAll.length != 0) {
					final double step = 100/usersDataAll.length * 0.01;
					double value = 0;	
					for (DbDataAll user : usersDataAll) {
						obs.setValue(value);
						value += step;
						final UserModel userModel = new UserModelImpl();
						userModel.setOrganization(ORGANIZATION);
						userModel.setInitials(user.getInitials());
						userModel.setFirstName(user.getName());
						userModel.setSecondName(user.getSurname());
						userModel.setThirdName(user.getPatronymic());
						userModel.setBirthday(user.getBirthday());
						userModel.setDepartament(user.getDepartamentAbbreviated());
						userModel.setIcon(user.getVCard());
						userModel.setPost(user.getPost());
						userModel.setPhoneNumber(user.getPhone());
						users.add(userModel);
					}
					obs.setValue(1);
				}
				obs.setValue(2);
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