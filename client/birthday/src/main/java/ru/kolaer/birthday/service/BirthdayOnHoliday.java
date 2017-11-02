package ru.kolaer.birthday.service;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Holiday;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.BirthdayInfoPane;
import ru.kolaer.birthday.mvp.view.BirthdayInfoPaneImpl;
import ru.kolaer.birthday.tools.BirthdayTools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BirthdayOnHoliday implements Service {
	private final BirthdayInfoPane birthdayInfoPane = new BirthdayInfoPaneImpl();
	private boolean tomorrow = false;
	private boolean afterTomorrow = false;
	
	@Override
	public void run() {
		NetworkUS usNetwork = UniformSystemEditorKitSingleton.getInstance().getUSNetwork();

		if(birthdayInfoPane.getContent() == null) {
			Tools.runOnWithOutThreadFX(() -> birthdayInfoPane.initView(BaseView::empty));
		}

		ServerResponse<List<Holiday>> holidays = usNetwork.getOtherPublicAPI().getHolidaysTable().getHolidaysInThisMonth();
		LocalDate now = LocalDate.now();

		if(now.getDayOfWeek().getValue() == 5) {
			if(!tomorrow) {
				showNotify("В субботу ", now.plusDays(1), new Holiday("выходной", now.format(BirthdayTools.getDateTimeFormatter()), TypeDay.HOLIDAY));
			}
			if(!afterTomorrow) {
				showNotify("В воскресенье ", now.plusDays(2), new Holiday("выходной", now.format(BirthdayTools.getDateTimeFormatter()), TypeDay.HOLIDAY));
			}
		}

		if(!holidays.isServerError()) {
			for (Holiday holiday : holidays.getResponse()) {
				LocalDate holidayLocalDate = LocalDate.parse(holiday.getDate(), BirthdayTools.getDateTimeFormatter());
				if (now.getDayOfMonth() == holidayLocalDate.getDayOfMonth()) {
					showNotify("Сегодня ", now, holiday);
				} else if (now.getDayOfMonth() + 1 == holidayLocalDate.getDayOfMonth()) {
					tomorrow = true;
					showNotify("Завтра ", now.plusDays(1), holiday);
				} else if (now.getDayOfMonth() + 2 == holidayLocalDate.getDayOfMonth()) {
					afterTomorrow = true;
					showNotify("После завтра ", now.plusDays(2), holiday);
				}
			}
		}
	}

	private void showNotify(String title, LocalDate date, Holiday holiday) {
		String newTitle = title + holiday.getName() + ".\nВ этот день празднуют:";

		ApplicationDataBase applicationDataBase = UniformSystemEditorKitSingleton.getInstance().getUSNetwork()
				.getKolaerWebServer()
				.getApplicationDataBase();

		ServerResponse<List<EmployeeDto>> employeeBirthdayTodayResponse = applicationDataBase
				.getGeneralEmployeesTable()
				.getUsersByBirthday(BirthdayTools.convertToDate(date));

		List<UserModel> users = new ArrayList<>();

		if (!employeeBirthdayTodayResponse.isServerError()) {
			List<EmployeeDto> employees = employeeBirthdayTodayResponse.getResponse();

			users.addAll(employees.stream()
					.map(UserModelImpl::new)
					.collect(Collectors.toList()));
		}

		ServerResponse<List<EmployeeOtherOrganizationDto>> otherEmployeeBirthdayTodayResponse = applicationDataBase
				.getEmployeeOtherOrganizationTable()
				.getUsersByBirthday(BirthdayTools.convertToDate(date));

		if (!otherEmployeeBirthdayTodayResponse.isServerError()) {
			List<EmployeeOtherOrganizationDto> employees = otherEmployeeBirthdayTodayResponse.getResponse();

			users.addAll(employees.stream()
					.map(UserModelImpl::new)
					.collect(Collectors.toList()));
		}

		if(users.size() > 0) {
			Tools.runOnWithOutThreadFX(() -> {
				birthdayInfoPane.put(newTitle, users);

				UniformSystemEditorKitSingleton
						.getInstance()
						.getUISystemUS()
						.getStatic().addStaticView(birthdayInfoPane);
			});
		}
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
