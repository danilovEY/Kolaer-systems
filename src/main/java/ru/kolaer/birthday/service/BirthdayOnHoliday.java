package ru.kolaer.birthday.service;

import java.time.LocalDate;
import javafx.application.Platform;
import javafx.util.Duration;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;
import ru.kolaer.birthday.tools.Tools;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.NotifyAction;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbBirthdayAll;
import ru.kolaer.server.dao.entities.DbDataAll;
import ru.kolaer.server.dao.entities.PublicHolidays;

public class BirthdayOnHoliday implements Service {	
	private final UniformSystemEditorKit editorKit;
	private boolean tomorrow = false;
	private boolean arterTomorrow = false;
	
	public BirthdayOnHoliday(final UniformSystemEditorKit editorKit) {
		this.editorKit = editorKit;
	}
	
	@Override
	public void run() {
		if(this.editorKit.getUSNetwork().getServerStatus() == ServerStatus.AVAILABLE) {
			final PublicHolidays[] holidays = this.editorKit.getUSNetwork().getOtherPublicAPI().getPublicHolidaysDateBase().getPublicHolidaysInThisMonth();
			final LocalDate date = LocalDate.now();
			for(final PublicHolidays holiday : holidays) {
				if(date.getDayOfMonth() == holiday.getDate().getDay()) {
					this.showNotify("Сегодня ", date, holiday);
				} else if(date.getDayOfMonth() + 1 == holiday.getDate().getDay()) {
					this.tomorrow = true;
					this.showNotify("Завтра ", date.plusDays(1), holiday);
				} else if(date.getDayOfMonth() + 2 == holiday.getDate().getDay()) {
					this.arterTomorrow = true;
					this.showNotify("После завтра ", date.plusDays(2), holiday);
				} 
			}
			
			if(date.getDayOfWeek().getValue() == 5 ) {
				if(!this.tomorrow) {
					this.showNotify("В субботу ", date.plusDays(1), new PublicHolidays(null, "выходной", "holiday", null));
				} 
				if(!this.arterTomorrow) {
					this.showNotify("В воскресенье ", date.plusDays(2), new PublicHolidays(null, "выходной", "holiday", null));
				}
			}
		}
	}

	private void showNotify(final String title, final LocalDate date, final PublicHolidays holiday) {
		final DbDataAll[] users = this.editorKit.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersByBirthday(Tools.convertToDate(date));
		final DbBirthdayAll[] usersBirthday = editorKit.getUSNetwork().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersByBirthday(Tools.convertToDate(date));
		
		final NotifyAction[] actions = new NotifyAction[users.length + usersBirthday.length];
		int i = 0;

		for(final DbDataAll user : users) {
			actions[i] = new NotifyAction(user.getInitials() + " (КолАЭР) " + user.getDepartamentAbbreviated(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});	
			});
			i++;
		}
		for(final DbBirthdayAll user : usersBirthday) {
			actions[i] = new NotifyAction(user.getInitials() + " ("+ Tools.getNameOrganization(user.getOrganization()) +") " + user.getDepartament(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				userModel.setOrganization(Tools.getNameOrganization(user.getOrganization()));
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});					
			});
			i++;
		}
		
		Platform.runLater(() -> {
			
			this.editorKit.getUISystemUS().getNotification().showSimpleNotify(title + holiday.getLocalName() + ". День рождения в этот день празднуют:", null, Duration.hours(24), actions);
		});
	}
	
	@Override
	public void setRunningStatus(boolean isRun) {
		
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public String getName() {
		return "Список деней рождений на праздниках";
	}

	@Override
	public void stop() {
		
	}
}
