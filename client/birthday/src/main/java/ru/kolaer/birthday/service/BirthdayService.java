package ru.kolaer.birthday.service;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;
import ru.kolaer.birthday.tools.Tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BirthdayService implements Service {
	private final Logger LOG = LoggerFactory.getLogger(BirthdayService.class);
	private final UniformSystemEditorKit editorKit;
	
	public BirthdayService(final UniformSystemEditorKit editorKid) {
		this.editorKit = editorKid;
	}
	
	@Override
	public void run() {
		ApplicationDataBase applicationDataBase = editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase();
		ServerResponse<List<EmployeeDto>> employeeBirthdayTodayResponse = applicationDataBase.getGeneralEmployeesTable()
				.getUsersBirthdayToday();
		ServerResponse<List<EmployeeOtherOrganizationDto>> otherEmployeeBirthdayTodayResponse = applicationDataBase
				.getEmployeeOtherOrganizationTable().getUsersBirthdayToday();

		if(employeeBirthdayTodayResponse.isServerError()) {
			editorKit.getUISystemUS().getNotification().showErrorNotify(employeeBirthdayTodayResponse.getExceptionMessage());
			return;
		}

		if(otherEmployeeBirthdayTodayResponse.isServerError()) {
			editorKit.getUISystemUS().getNotification().showErrorNotify(otherEmployeeBirthdayTodayResponse.getExceptionMessage());
			return;
		}

		if(employeeBirthdayTodayResponse.isServerError() || otherEmployeeBirthdayTodayResponse.isServerError()) {
			return;
		}

		List<EmployeeDto> employees = employeeBirthdayTodayResponse.getResponse();
		List<EmployeeOtherOrganizationDto> otherEmployees = otherEmployeeBirthdayTodayResponse.getResponse();

		List<NotifyAction> actions = new ArrayList<>(employees.size() + otherEmployees.size());

		for(EmployeeDto user : employees) {
			actions.add(new NotifyAction(user.getInitials() + " (КолАЭР) " + user.getDepartment().getAbbreviatedName(), e -> {
				final UserModel userModel = new UserModelImpl(user);

				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});
			}));
		}
		for(EmployeeOtherOrganizationDto user : otherEmployees) {
			actions.add(new NotifyAction(user.getInitials() + " ("+ Tools.getNameOrganization(user.getOrganization()) +") " + user.getDepartment(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				userModel.setOrganization(Tools.getNameOrganization(user.getOrganization()));
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});
			}));
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		StringBuilder title = new StringBuilder("Сегодня \"").append(LocalDate.now().format(formatter)).append("\".");

		try{
			TimeUnit.SECONDS.sleep(1);
		}catch(InterruptedException e){
			LOG.error("Привышено ожижание", e);
		}

		Platform.runLater(() -> {
			this.editorKit.getUISystemUS().getNotification().showInformationNotify(title.toString(), "Поздравляем с днем рождения!", null, actions);
		});
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public String getName() {
		return "Сегодняшние дни рождения";
	}

	@Override
	public void stop() {
		
	}
}
