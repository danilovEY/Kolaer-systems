package ru.kolaer.birthday.mvp.presenter.impl;

import ru.kolaer.api.mvp.model.DbDataAll;
import ru.kolaer.api.system.DefaultProgressBar;
import ru.kolaer.api.system.ProgressBarObservable;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.PCalendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Presenter календаря КолАЕРа.
 *
 * @author danilovey
 * @version 0.1
 */
public class PCalendarKAER extends PCalendarBase implements PCalendar {
	
	public PCalendarKAER(final UniformSystemEditorKit editorKid) {
		super("КолАтомэнергоремонт", editorKid);
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
			this.view.setDayCellFactory(new CustomCallback(editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase()));
			this.isInitDayCellFactory = true;
		}
	}
}