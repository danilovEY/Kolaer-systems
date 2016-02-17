package ru.kolaer.birthday.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.util.Duration;
import ru.kolaer.client.javafx.services.Service;
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
		try {
			if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.AVAILABLE) {
				final DbDataAll[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
				final DbBirthdayAll[] usersBirthday = editorKid.getUSNetwork().getKolaerDataBase().getUserBirthdayAllDataBase().getUsersBirthdayToday();
				
				final StringBuilder todayBirthday = new StringBuilder();
				for(DbDataAll user : users) {
					todayBirthday.append(user.getInitials()).append(" (").append("КолАЭР").append(") - ").append(user.getDepartamentAbbreviated()).append("\n");
				}
				for(DbBirthdayAll user : usersBirthday) {
					todayBirthday.append(user.getInitials()).append(" (").append(this.getNameOrganization(user.getOrganization())).append(") - ").append(user.getDepartament()).append("\n");
				}
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				final StringBuilder title = new StringBuilder("Сегодня \"").append(LocalDate.now().format(formatter)).append("\". Поздравляем с днем рождения!\n");
				
				this.editorKid.getUISystemUS().getNotification().showInformationNotify(title.toString(), todayBirthday.toString(), Duration.hours(1));
			}
		} catch(final Exception ex) {
			LOG.error("Ошибка при отображении информации!", ex);
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
