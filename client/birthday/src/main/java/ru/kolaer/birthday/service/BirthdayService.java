package ru.kolaer.birthday.service;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.restful.DbBirthdayAll;
import ru.kolaer.api.mvp.model.restful.DbDataAll;
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

public class BirthdayService implements Service {
	private final Logger LOG = LoggerFactory.getLogger(BirthdayService.class);
	private final UniformSystemEditorKit editorKit;
	
	public BirthdayService(final UniformSystemEditorKit editorKid) {
		this.editorKit = editorKid;
	}
	
	@Override
	public void run() {
		if(this.editorKit.getUSNetwork().getRestfulServer().getServerStatus() == ServerStatus.AVAILABLE) {
			final DbDataAll[] users = this.editorKit.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
			final DbBirthdayAll[] usersBirthday = editorKit.getUSNetwork().getRestfulServer().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersBirthdayToday();
			
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
			for(final DbBirthdayAll user : usersBirthday) {
				actions[i] = new NotifiAction(user.getInitials() + " ("+ Tools.getNameOrganization(user.getOrganization()) +") " + user.getDepartament(), e -> {
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
