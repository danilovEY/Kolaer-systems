package ru.kolaer.birthday.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.util.Duration;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMDetailedInformationStageImpl;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.NotifyAction;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbBirthdayAll;
import ru.kolaer.server.dao.entities.DbDataAll;

public class BirthdayService implements Service {
	private final Logger LOG = LoggerFactory.getLogger(BirthdayService.class);
	private final UniformSystemEditorKit editorKid;
	
	public BirthdayService(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
	}
	
	@Override
	public void run() {
		if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.AVAILABLE) {
			final DbDataAll[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
			final DbBirthdayAll[] usersBirthday = editorKid.getUSNetwork().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersBirthdayToday();
			
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
				actions[i] = new NotifyAction(user.getInitials() + " ("+ this.getNameOrganization(user.getOrganization()) +") " + user.getDepartament(), e -> {
					final UserModel userModel = new UserModelImpl(user);
					userModel.setOrganization(this.getNameOrganization(user.getOrganization()));
					Platform.runLater(() -> {
						new VMDetailedInformationStageImpl(userModel).show();
					});					
				});
				i++;
			}
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			final StringBuilder title = new StringBuilder("Сегодня \"").append(LocalDate.now().format(formatter)).append("\". Поздравляем с днем рождения!\n");
			
			Platform.runLater(() -> {
				this.editorKid.getUISystemUS().getNotification().showSimpleNotify(title.toString(), null, Duration.hours(1), actions);
			});
		}
	}

	private String getNameOrganization(final String org) {
		switch(org) {		
			case "БалаковоАтомэнергоремонт": return "БалАЭР";
			case "ВолгодонскАтомэнергоремонт": return "ВДАЭР";
			case "КалининАтомэнергоремонт": return "КАЭР";
			case "КурскАтомэнергоремонт": return "КурскАЭР";
			case "ЛенАтомэнергоремонт": return "ЛенАЭР";
			case "НововоронежАтомэнергоремонт": return "НВАЭР";
			case "СмоленскАтомэнергоремонт": return "САЭР";
			case "УралАтомэнергоремонт": return "УралАЭР";
			case "Центральный аппарат": return "ЦА";
			case "КолАтомэнергоремонт": return "КолАЭР";
			default: return org;
		}
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
		return "Сегодняшние дни рождения";
	}

	@Override
	public void stop() {
		
	}
}
