package ru.kolaer.birthday.service;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.BirthdayInfoPane;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BirthdayService implements Service {
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
						String title = "Поздравляем с днем рождения сотрудников в филиалах!";

						Tools.runOnWithOutThreadFX(() -> new BirthdayInfoPane(title, employees.stream()
								.map(UserModelImpl::new)
								.collect(Collectors.toList()))
								.initView(UniformSystemEditorKitSingleton
										.getInstance()
										.getUISystemUS()
										.getStatic()::addStaticView));
					}
					lastUpdateOtherEmployee = now;
				}
			}

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
						String title = "Поздравляем с днем рождения наших сотрудников!";

						Tools.runOnWithOutThreadFX(() -> new BirthdayInfoPane(title, employees.stream()
								.map(UserModelImpl::new)
								.collect(Collectors.toList()))
								.initView(UniformSystemEditorKitSingleton
										.getInstance()
										.getUISystemUS()
										.getStatic()::addStaticView));
					}
					lastUpdateEmployee = now;
				}
			}

			try{
				TimeUnit.MINUTES.sleep(5);
			}catch(InterruptedException e){
				run = false;
			}
		}
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
