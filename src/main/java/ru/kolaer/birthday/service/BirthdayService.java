package ru.kolaer.birthday.service;

import java.time.LocalDate;

import javafx.util.Duration;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class BirthdayService implements Service {
	private final UniformSystemEditorKit editorKid;
	
	public BirthdayService(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
	}
	
	@Override
	public void run() {
		if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.AVAILABLE) {
			DbDataAll[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
			final StringBuilder todayBirthday = new StringBuilder();
			for(DbDataAll user : users) {
				if(this.checkUser(user)) {
					todayBirthday.append(user.getInitials()).append(" (").append(user.getPhone()).append(") - ").append(user.getDepartamentAbbreviated()).append("\n");
				}
			}
			final StringBuilder title = new StringBuilder("Сегодня \"").append(LocalDate.now()).append("\". Поздравляем с днем рождения!\n");
			this.editorKid.getUISystemUS().getNotification().showSimpleNotify(title.toString(), todayBirthday.toString(), Duration.hours(1));
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
	

	private boolean checkUser(final DbDataAll user) {
		if(user.getCategoryUnit().equals("Рабочий"))
			return false;
		
		return true;
		
	}
}
