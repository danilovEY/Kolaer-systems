package ru.kolaer.birthday.service;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.ui.NotifiAction;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;
import ru.kolaer.birthday.tools.Tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class BirthdayService implements Service {
	private final Logger LOG = LoggerFactory.getLogger(BirthdayService.class);
	private final UniformSystemEditorKit editorKit;
	
	public BirthdayService(final UniformSystemEditorKit editorKid) {
		this.editorKit = editorKid;
	}
	
	@Override
	public void run() {
		//final DbDataAll[] users = this.editorKit.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
		final EmployeeEntity[]  employeesEntities = this.editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable().getUsersBirthdayToday();
		final EmployeeOtherOrganization[] usersBirthday = editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getEmployeeOtherOrganizationTable().getUsersBirthdayToday();

		final NotifiAction[] actions = new NotifiAction[employeesEntities.length + usersBirthday.length];
		int i = 0;

		for(final EmployeeEntity user : employeesEntities) {
			actions[i] = new NotifiAction(user.getInitials() + " (КолАЭР) " + user.getDepartment().getAbbreviatedName(), e -> {
				final UserModel userModel = new UserModelImpl(user);

				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});
			});
			i++;
		}
		for(final EmployeeOtherOrganization user : usersBirthday) {
			actions[i] = new NotifiAction(user.getInitials() + " ("+ Tools.getNameOrganization(user.getOrganization()) +") " + user.getDepartment(), e -> {
				final UserModel userModel = new UserModelImpl(user);
				userModel.setOrganization(Tools.getNameOrganization(user.getOrganization()));
				Platform.runLater(() -> {
					new VMDetailedInformationStageImpl(userModel).show();
				});
			});
			i++;
		}
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		final StringBuilder title = new StringBuilder("Сегодня \"").append(LocalDate.now().format(formatter)).append("\".");

		try{
			TimeUnit.SECONDS.sleep(1);
		}catch(InterruptedException e){
			LOG.error("Привышено ожижание", e);
		}

		Platform.runLater(() -> {
			this.editorKit.getUISystemUS().getNotification().showInformationNotifi(title.toString(), "Поздравляем с днем рождения!", null, actions);
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
