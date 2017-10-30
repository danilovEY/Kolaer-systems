package ru.kolaer.birthday.service;

import javafx.util.Duration;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.DetailedInformationVc;
import ru.kolaer.birthday.tools.BirthdayTools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BirthdayOnHoliday implements Service {
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private boolean tomorrow = false;
	private boolean afterTomorrow = false;
	
	@Override
	public void run() {
		NetworkUS usNetwork = UniformSystemEditorKitSingleton.getInstance().getUSNetwork();

		final ServerResponse<List<Holiday>> holidays = usNetwork.getOtherPublicAPI().getHolidaysTable().getHolidaysInThisMonth();
		final LocalDate date = LocalDate.now();

		if(date.getDayOfWeek().getValue() == 5 ) {
			if(!this.tomorrow) {
				this.showNotify("В субботу ", date.plusDays(1), new Holiday("выходной", date.format(this.dtf), TypeDay.HOLIDAY));
			}
			if(!this.afterTomorrow) {
				this.showNotify("В воскресенье ", date.plusDays(2), new Holiday("выходной", date.format(this.dtf), TypeDay.HOLIDAY));
			}
		}

		for(final Holiday holiday : holidays.getResponse()) {
			final LocalDate holidayLocalDate = LocalDate.parse(holiday.getDate(), dtf);
			if(date.getDayOfMonth() == holidayLocalDate.getDayOfMonth()) {
				this.showNotify("Сегодня ", date, holiday);
			} else if(date.getDayOfMonth() + 1 == holidayLocalDate.getDayOfMonth()) {
				this.tomorrow = true;
				this.showNotify("Завтра ", date.plusDays(1), holiday);
			} else if(date.getDayOfMonth() + 2 == holidayLocalDate.getDayOfMonth()) {
				this.afterTomorrow = true;
				this.showNotify("После завтра ", date.plusDays(2), holiday);
			} if(date.getDayOfMonth() + 3 == holidayLocalDate.getDayOfMonth()) {
				this.showNotify("Через 3 дня ", date.plusDays(3), holiday);
			} if(date.getDayOfMonth() + 4 == holidayLocalDate.getDayOfMonth()) {
				this.showNotify("Через 4 дня ", date.plusDays(4), holiday);
			}
		}
	}

	private void showNotify(String title, LocalDate date, Holiday holiday) {
		ApplicationDataBase applicationDataBase = UniformSystemEditorKitSingleton.getInstance().getUSNetwork()
				.getKolaerWebServer()
				.getApplicationDataBase();

		ServerResponse<List<EmployeeDto>> employeesResponse = applicationDataBase.getGeneralEmployeesTable()
				.getUsersByBirthday(BirthdayTools.convertToDate(date));
		ServerResponse<List<EmployeeOtherOrganizationDto>> otherEmployeesResponse = applicationDataBase.getEmployeeOtherOrganizationTable()
				.getUsersByBirthday(BirthdayTools.convertToDate(date));

		List<NotifyAction> actions = new ArrayList<>();

		if(!employeesResponse.isServerError()) {
			for(EmployeeDto user : employeesResponse.getResponse()) {
				actions.add(new NotifyAction(user.getInitials() + " (КолАЭР) " + user.getDepartment().getAbbreviatedName(),
						e -> new DetailedInformationVc(new UserModelImpl(user)).show()
				));
			}
		}

		if(!otherEmployeesResponse.isServerError()) {
			for (EmployeeOtherOrganizationDto user : otherEmployeesResponse.getResponse()) {
				actions.add(new NotifyAction(user.getInitials() + " (" + BirthdayTools.getNameOrganization(user.getOrganization()) + ") " + user.getDepartment(), e -> {
					final UserModel userModel = new UserModelImpl(user);
					userModel.setOrganization(BirthdayTools.getNameOrganization(user.getOrganization()));
					new DetailedInformationVc(userModel).show();
				}));
			}
		}

		ru.kolaer.api.tools.Tools.runOnWithOutThreadFX(() -> {
			UniformSystemEditorKitSingleton.getInstance()
					.getUISystemUS()
					.getNotification()
					.showInformationNotify(title + holiday.getName() + ".", "День рождения в этот день празднуют:",
							Duration.hours(24),
							actions);
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
