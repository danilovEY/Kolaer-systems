package ru.kolaer.birthday.mvp.presenter.impl;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
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
				
				ServerResponse<List<EmployeeOtherOrganizationDto>> usersBirthdayAll = this.editorKid.getUSNetwork()
						.getKolaerWebServer().getApplicationDataBase()
						.getEmployeeOtherOrganizationTable()
						.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				
				ServerResponse<List<EmployeeDto>> usersDataAll = this.editorKid.getUSNetwork().getKolaerWebServer()
						.getApplicationDataBase().getGeneralEmployeesTable()
						.getUsersByBirthday(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				
				obs.setValue(-1);
				int sizeOtherEmployee = usersBirthdayAll.getResponse().size();
				int sizeEmployee = usersDataAll.getResponse().size();
				if(sizeEmployee != 0) {
					final double step = 100/(sizeOtherEmployee + sizeEmployee) * 0.01;
					double value = 0;	
					
					for (EmployeeDto user : usersDataAll.getResponse()) {
						obs.setValue(value);
						value += step;
						final UserModel userModel = new UserModelImpl(user);
						users.add(userModel);
					}
					
					for (EmployeeOtherOrganizationDto user : usersBirthdayAll.getResponse()) {
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
			this.view.setDayCellFactory(new CustomCallback(editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable(), editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getEmployeeOtherOrganizationTable()));
			this.isInitDayCellFactory = true;
		}
	}
}
