package ru.kolaer.birthday.mvp.presenter.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.api.system.DefaultProgressBar;
import ru.kolaer.api.system.ProgressBarObservable;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.dao.entities.DbBirthdayAll;

/**
 * Календарь для филиалов.
 *
 * @author danilovey
 * @version 0.1
 */
public class PCalendarAffiliates extends PCalendarBase  {

	public PCalendarAffiliates(String organization, UniformSystemEditorKit editorKid) {
		super(organization, editorKid);
	}

	@Override
	public void notifySelectedDate(LocalDate date) {
		if (this.observerCalendar != null) {
			final List<UserModel> users = new ArrayList<>();
			if (this.editorKid != null) {
				final ProgressBarObservable obs = new DefaultProgressBar();
				this.editorKid.getUISystemUS().getStatusBar().addProgressBar(obs);
				final DbBirthdayAll[] usersDataAll = this.editorKid.getUSNetwork().getKolaerDataBase()
						.getUserBirthdayAllDataBase()
						.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), ORGANIZATION);
				obs.setValue(-1);
				if(usersDataAll.length != 0) {
					final double step = 100/usersDataAll.length * 0.01;
					double value = 0;	
					for (DbBirthdayAll user : usersDataAll) {
						obs.setValue(value);
						value += step;
						final UserModel userModel = new UserModelImpl(user);
						userModel.setOrganization(ORGANIZATION);
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
			this.view.setDayCellFactory(new CustomCallback(editorKid.getUSNetwork().getKolaerDataBase().getUserBirthdayAllDataBase(), ORGANIZATION));	
			this.isInitDayCellFactory = true;
		}
	}	
}