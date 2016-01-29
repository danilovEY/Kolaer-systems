package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBirthdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthdayObserver;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class VMTableWithUsersBithdayObserverImpl implements VMTableWithUsersBirthdayObserver{
	private final VTableWithUsersBirthday table = new VTableWithUsersBirthdayImpl();
	private final Logger LOG = LoggerFactory.getLogger(VMTableWithUsersBithdayObserverImpl.class);
	private final UniformSystemEditorKit editorKid;
	
	public VMTableWithUsersBithdayObserverImpl() {
		this(null);
	}
	
	
	public VMTableWithUsersBithdayObserverImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.initWithEditorKid();
	}

	private void initWithEditorKid()  {
		final List<UserModel> userModelList = new ArrayList<>();
		CompletableFuture.runAsync(() -> {
			DbDataAll[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
			
			for(DbDataAll user : users) {
				final UserModel userModel = new UserModelImpl();
				userModel.setOrganization("КолАЭР");
				userModel.setFirstName(user.getName());
				userModel.setSecondName(user.getSurname());
				userModel.setThirdName(user.getPatronymic());
				userModel.setBirthday(user.getBirthday());
				userModel.setDepartament(user.getDepartamentAbbreviated());
				userModel.setPhoneNumber(user.getPhone());
				userModel.setIcon(user.getVCard());
				userModelList.add(userModel);
			}
			
			this.table.setData(userModelList);
		}).exceptionally(t -> {
			LOG.error("Ошибка!", t);
			return null;
		});
	}

	@Override
	public VTableWithUsersBirthday getView() {
		return this.table;
	}

	@Override
	public void updateSelectedDate(LocalDate date, List<UserModel> users) {
		this.table.setData(users);
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		this.table.setTitle("\"" + date.format(formatter) + "\" день рождения у:");
	}
	
}
