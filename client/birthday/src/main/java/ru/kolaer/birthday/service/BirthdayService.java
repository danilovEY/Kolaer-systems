package ru.kolaer.birthday.service;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.DetailedInformationVc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BirthdayService implements Service {
	private final Logger log = LoggerFactory.getLogger(BirthdayService.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
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

			/*

			ServerResponse<List<EmployeeOtherOrganizationDto>> otherEmployeeBirthdayTodayResponse = applicationDataBase
						.getEmployeeOtherOrganizationTable()
						.getUsersBirthdayToday();

				if(employeeBirthdayTodayResponse.isServerError() || otherEmployeeBirthdayTodayResponse.isServerError()) {
					return;
				}

				List<EmployeeOtherOrganizationDto> otherEmployees = otherEmployeeBirthdayTodayResponse.getResponse();
				for(EmployeeOtherOrganizationDto user : otherEmployees) {
					actions.add(new NotifyAction(user.getInitials() + " ("+ Tools.getNameOrganization(user.getOrganization()) +") " + user.getDepartment(), e -> {
						final UserModel userModel = new UserModelImpl(user);
						userModel.setOrganization(Tools.getNameOrganization(user.getOrganization()));
						Platform.runLater(() -> {
							new VMDetailedInformationStageImpl(userModel).show();
						});
					}));
				}

			 */

			if(lastUpdateEmployee == null || lastUpdateEmployee.isBefore(now)) {
				ServerResponse<List<EmployeeDto>> employeeBirthdayTodayResponse = applicationDataBase
						.getGeneralEmployeesTable()
						.getUsersBirthdayToday();

				if(employeeBirthdayTodayResponse.isServerError()) {
					UniformSystemEditorKitSingleton.getInstance()
							.getUISystemUS()
							.getNotification()
							.showErrorNotify(employeeBirthdayTodayResponse.getExceptionMessage());

					lastUpdateEmployee = null;
				} else {
					List<EmployeeDto> employees = employeeBirthdayTodayResponse.getResponse();

					List<NotifyAction> actions = new ArrayList<>(employees.size() + employees.size());

					for(EmployeeDto user : employees) {
						actions.add(new NotifyAction(user.getInitials() + " (КолАЭР) " + user.getDepartment().getAbbreviatedName(), e -> {
							UserModel userModel = new UserModelImpl(user);

							new DetailedInformationVc(userModel).show();
						}));
					}

					lastUpdateEmployee = now;
				}




				try{
					TimeUnit.MINUTES.sleep(5);
				}catch(InterruptedException e){
					log.error("Привышено ожижание", e);
				}

				Platform.runLater(() -> {
					this.editorKit.getUISystemUS().getNotification().showInformationNotify(title.toString(), "Поздравляем с днем рождения!", null, actions);
				});
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
