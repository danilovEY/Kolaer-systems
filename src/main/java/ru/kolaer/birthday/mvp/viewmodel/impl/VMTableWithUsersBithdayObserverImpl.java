package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBirthdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthdayObserver;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class VMTableWithUsersBithdayObserverImpl implements VMTableWithUsersBirthdayObserver{
	private final VTableWithUsersBirthday table = new VTableWithUsersBirthdayImpl();
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
		DbDataAll[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
		
		for(DbDataAll user : users) {
			final UserModel userModel = new UserModelImpl();
			userModel.setPersonNumber(user.getPersonNumber().intValue());
			userModel.setFirstName(user.getName());
			userModel.setSecondName(user.getSurname());
			userModel.setThirdName(user.getPatronymic());
			userModel.setBirthday(user.getBirthday());
			userModel.setDepartament(user.getDepartamentAbbreviated());
			userModel.setIcon(user.getVCard());
			userModelList.add(userModel);
		}
		
		this.table.setData(userModelList);
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
