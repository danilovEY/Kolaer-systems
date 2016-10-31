package ru.kolaer.birthday.service;

import javafx.application.Platform;
import javafx.util.Duration;
import ru.kolaer.api.mvp.model.restful.EmployeeOtherOrganizationBase;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
import ru.kolaer.api.mvp.model.restful.PublicHolidays;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.ui.NotifiAction;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;
import ru.kolaer.birthday.tools.Tools;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class BirthdayOnHoliday implements Service {
	private final UniformSystemEditorKit editorKit;
	private boolean tomorrow = false;
	private boolean arterTomorrow = false;
	public BirthdayOnHoliday(final UniformSystemEditorKit editorKit) {
		this.editorKit = editorKit;
	}
	
	@Override
	public void run() {
		if(this.editorKit.getUSNetwork().getRestfulServer().getServerStatus() == ServerStatus.AVAILABLE) {
			final PublicHolidays[] holidays = this.editorKit.getUSNetwork().getOtherPublicAPI().getPublicHolidaysDateBase().getPublicHolidaysInThisMonth();
			final LocalDate date = LocalDate.now();
			if(date.getDayOfWeek().getValue() == 5 ) {
				if(!this.tomorrow) {
					this.showNotifi("В субботу ", date.plusDays(1), new PublicHolidays(null, "выходной", "holiday", null));
				} 
				if(!this.arterTomorrow) {
					this.showNotifi("В воскресенье ", date.plusDays(2), new PublicHolidays(null, "выходной", "holiday", null));
				}
			}
			
			for(final PublicHolidays holiday : holidays) {
				if(date.getDayOfMonth() == holiday.getDate().getDay()) {
					this.showNotifi("Сегодня ", date, holiday);
				} else if(date.getDayOfMonth() + 1 == holiday.getDate().getDay()) {
					this.tomorrow = true;
					this.showNotifi("Завтра ", date.plusDays(1), holiday);
				} else if(date.getDayOfMonth() + 2 == holiday.getDate().getDay()) {
					this.arterTomorrow = true;
					this.showNotifi("После завтра ", date.plusDays(2), holiday);
				} if(date.getDayOfMonth() + 3 == holiday.getDate().getDay()) {
					this.showNotifi("Через 3 дня ", date.plusDays(3), holiday);
				} if(date.getDayOfMonth() + 4 == holiday.getDate().getDay()) {
					this.showNotifi("Через 4 дня ", date.plusDays(4), holiday);
				}
			}
		}
	}

	private void showNotifi(final String title, final LocalDate date, final PublicHolidays holiday) {
		final DbDataAll[] users = this.editorKit.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserDataAllDataBase().getUsersByBirthday(Tools.convertToDate(date));
		final EmployeeOtherOrganizationBase[] usersBirthday = editorKit.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersByBirthday(Tools.convertToDate(date));
		
		final NotifiAction[] actions = new NotifiAction[users.length + usersBirthday.length];
		int i = 0;

		for(final DbDataAll user : users) {
			actions[i] = new NotifiAction(user.getInitials() + " (КолАЭР) " + user.getDepartamentAbbreviated(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});	
			});
			i++;
		}
		for(final EmployeeOtherOrganizationBase user : usersBirthday) {
			actions[i] = new NotifiAction(user.getInitials() + " ("+ Tools.getNameOrganization(user.getOrganization()) +") " + user.getDepartament(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				userModel.setOrganization(Tools.getNameOrganization(user.getOrganization()));
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});					
			});
			i++;
		}
		
		try{
			TimeUnit.SECONDS.sleep(1);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		Platform.runLater(() -> {
			this.editorKit.getUISystemUS().getNotification().showInformationNotifi(title + holiday.getLocalName() + ".", "День рождения в этот день празднуют:", Duration.hours(24), actions);
		});
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
