package ru.kolaer.birthday.mvp.presenter.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.client.javafx.system.DefaultProgressBar;
import ru.kolaer.client.javafx.system.ProgressBarObservable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

/**
 * Presenter календаря КолАЕРа.
 *
 * @author danilovey
 * @version 0.1
 */
public class PCalendarKAER extends PCalendarBase implements PCalendar {
	
	public PCalendarKAER(final UniformSystemEditorKit editorKid) {
		super("КолАтомэнергоремонт", editorKid);
		this.view.setDayCellFactory(new CustomCallback(editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase()));	
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
}