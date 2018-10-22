package ru.kolaer.birthday.service;

import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.common.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.common.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.plugins.services.Service;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.common.tools.Tools;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.BirthdayInfoPane;
import ru.kolaer.birthday.mvp.view.BirthdayInfoPaneImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BirthdayService implements Service {
	private final BirthdayInfoPane birthdayInfoPane = new BirthdayInfoPaneImpl();
	private boolean run = false;
	private LocalDate lastUpdateEmployee;
	private LocalDate lastUpdateOtherEmployee;

	@Override
	public void run() {
		run = true;

		while (run) {
			LocalDate now = LocalDate.now();

			ApplicationDataBase applicationDataBase = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase();

			if(lastUpdateEmployee == null || lastUpdateEmployee.isBefore(now)) {
				ServerResponse<List<EmployeeDto>> employeeBirthdayTodayResponse = applicationDataBase
						.getGeneralEmployeesTable()
						.getUsersBirthdayToday();

				if (employeeBirthdayTodayResponse.isServerError()) {
					UniformSystemEditorKitSingleton.getInstance()
							.getUISystemUS()
							.getNotification()
							.showErrorNotify(employeeBirthdayTodayResponse.getExceptionMessage());

					lastUpdateEmployee = null;
				} else {
					List<EmployeeDto> employees = employeeBirthdayTodayResponse.getResponse();

					if (employees.size() > 0) {
						String title = "Сегодня день рождения у наших сотрудников!";

						loadEmployees(title, employees.stream()
								.map(UserModelImpl::new)
								.collect(Collectors.toList()));
					}
					lastUpdateEmployee = now;
				}
			}

			if(lastUpdateOtherEmployee == null || lastUpdateOtherEmployee.isBefore(now)) {
				ServerResponse<List<EmployeeOtherOrganizationDto>> otherEmployeeBirthdayTodayResponse = applicationDataBase
						.getEmployeeOtherOrganizationTable()
						.getUsersBirthdayToday();

				if (otherEmployeeBirthdayTodayResponse.isServerError()) {
					UniformSystemEditorKitSingleton.getInstance()
							.getUISystemUS()
							.getNotification()
							.showErrorNotify(otherEmployeeBirthdayTodayResponse.getExceptionMessage());

					lastUpdateOtherEmployee = null;
				} else {
					List<EmployeeOtherOrganizationDto> employees = otherEmployeeBirthdayTodayResponse.getResponse();

					if (employees.size() > 0) {
						String title = "Сегодня день рождения у сотрудников филиалов!";

						loadEmployees(title, employees.stream()
								.map(UserModelImpl::new)
								.collect(Collectors.toList()));
					}
					lastUpdateOtherEmployee = now;
				}
			}

			try{
				TimeUnit.HOURS.sleep(1);
			}catch(InterruptedException e){
				run = false;
			}
		}
	}

	private void loadEmployees(String title, List<UserModel> userModels) {
		Tools.runOnWithOutThreadFX(() -> {
			if(!birthdayInfoPane.isViewInit()) {
				birthdayInfoPane.initView(BaseView::empty);

				birthdayInfoPane.put(title, userModels);

				UniformSystemEditorKitSingleton
						.getInstance()
						.getUISystemUS()
						.getStatic().addStaticView(birthdayInfoPane);
			} else {
				birthdayInfoPane.put(title, userModels);
			}
		});
	}

	@Override
	public boolean isRunning() {
		return run;
	}

	@Override
	public String getName() {
		return "Сегодняшние дни рождения";
	}

	@Override
	public void stop() {
		run = false;
	}
}
