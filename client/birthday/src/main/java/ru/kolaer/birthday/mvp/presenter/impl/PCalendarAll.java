package ru.kolaer.birthday.mvp.presenter.impl;

import ru.kolaer.api.mvp.model.restful.DbBirthdayAll;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.ui.DefaultProgressBar;
import ru.kolaer.api.system.ui.ProgressBarObservable;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PCalendarAll extends PCalendarBase {
	public PCalendarAll(UniformSystemEditorKit editorKid) {
		super("Все", editorKid);
	}

	@Override
	public void notifySelectedDate(LocalDate date) {
		if (this.observerCalendar != null) {
			final List<UserModel> users = new ArrayList<>();
			if (this.editorKid != null) {
				final ProgressBarObservable obs = new DefaultProgressBar();
				this.editorKid.getUISystemUS().getStatusBar().addProgressBar(obs);
				
				final DbBirthdayAll[] usersBirthdayAll = this.editorKid.getUSNetwork().getRestfulServer().getKolaerDataBase()
						.getUserBirthdayAllDataBase()
						.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				
				final DbDataAll[] usersDataAll = this.editorKid.getUSNetwork().getRestfulServer().getKolaerDataBase()
						.getUserDataAllDataBase()
						.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				
				obs.setValue(-1);
				if(usersBirthdayAll.length != 0) {
					final double step = 100/(usersBirthdayAll.length + usersDataAll.length) * 0.01;
					double value = 0;	
					
					for (DbDataAll user : usersDataAll) {
						obs.setValue(value);
						value += step;
						final UserModel userModel = new UserModelImpl(user);
						users.add(userModel);
					}
					
					for (DbBirthdayAll user : usersBirthdayAll) {
						obs.setValue(value);
						value += step;
						final UserModel userModel = new UserModelImpl(user);
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
	public void initDayCellFactory() {
		if(!this.isInitDayCellFactory) {
			this.view.setDayCellFactory(new CustomCallback(editorKid.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserDataAllDataBase(), editorKid.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserBirthdayAllDataBase()));
			this.isInitDayCellFactory = true;
		}
	}
}
