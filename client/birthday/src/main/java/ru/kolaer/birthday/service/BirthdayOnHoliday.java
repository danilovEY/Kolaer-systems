package ru.kolaer.birthday.service;

import javafx.application.Platform;
import javafx.util.Duration;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
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
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class BirthdayOnHoliday implements Service {
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private final UniformSystemEditorKit editorKit;
	private boolean tomorrow = false;
	private boolean arterTomorrow = false;
	public BirthdayOnHoliday(final UniformSystemEditorKit editorKit) {
		this.editorKit = editorKit;
	}
	
	@Override
	public void run() {
		//final PublicHolidays[] holidays = this.editorKit.getUSNetwork().getOtherPublicAPI().getPublicHolidaysDateBase().getPublicHolidaysInThisMonth();

		final Holiday[] holidays = this.editorKit.getUSNetwork().getOtherPublicAPI().getHolidaysTable().getHolidaysInThisMonth();
		final LocalDate date = LocalDate.now();

		if(date.getDayOfWeek().getValue() == 5 ) {
			if(!this.tomorrow) {
				this.showNotifi("В субботу ", date.plusDays(1), new Holiday("выходной", date.format(this.dtf), TypeDay.HOLIDAY));
			}
			if(!this.arterTomorrow) {
				this.showNotifi("В воскресенье ", date.plusDays(2), new Holiday("выходной", date.format(this.dtf), TypeDay.HOLIDAY));
			}
		}

		for(final Holiday holiday : holidays) {
			final LocalDate holidayLocalDate = LocalDate.parse(holiday.getDate(), dtf);
			if(date.getDayOfMonth() == holidayLocalDate.getDayOfMonth()) {
				this.showNotifi("Сегодня ", date, holiday);
			} else if(date.getDayOfMonth() + 1 == holidayLocalDate.getDayOfMonth()) {
				this.tomorrow = true;
				this.showNotifi("Завтра ", date.plusDays(1), holiday);
			} else if(date.getDayOfMonth() + 2 == holidayLocalDate.getDayOfMonth()) {
				this.arterTomorrow = true;
				this.showNotifi("После завтра ", date.plusDays(2), holiday);
			} if(date.getDayOfMonth() + 3 == holidayLocalDate.getDayOfMonth()) {
				this.showNotifi("Через 3 дня ", date.plusDays(3), holiday);
			} if(date.getDayOfMonth() + 4 == holidayLocalDate.getDayOfMonth()) {
				this.showNotifi("Через 4 дня ", date.plusDays(4), holiday);
			}
		}
	}

	private void showNotifi(final String title, final LocalDate date, final Holiday holiday) {
		//final DbDataAll[] users = this.editorKit.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserDataAllDataBase().getUsersByBirthday(Tools.convertToDate(date));
		final GeneralEmployeesEntity[] employeesEntities = this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable().getUsersByBirthday(Tools.convertToDate(date));
		final EmployeeOtherOrganization[] usersBirthday = editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getEmployeeOtherOrganizationTable().getUsersByBirthday(Tools.convertToDate(date));
		
		final NotifiAction[] actions = new NotifiAction[employeesEntities.length + usersBirthday.length];
		int i = 0;

		for(final GeneralEmployeesEntity user : employeesEntities) {
			actions[i] = new NotifiAction(user.getInitials() + " (КолАЭР) " + user.getDepartament().getAbbreviatedName(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});	
			});
			i++;
		}
		for(final EmployeeOtherOrganization user : usersBirthday) {
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
			this.editorKit.getUISystemUS().getNotification().showInformationNotifi(title + holiday.getName() + ".", "День рождения в этот день празднуют:", Duration.hours(24), actions);
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
