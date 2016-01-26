package ru.kolaer.birthday.mvp.viewmodel.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBirthdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthday;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbDataAll;

public class VMTableWithUsersBithdayImpl implements VMTableWithUsersBirthday{
	private final VTableWithUsersBirthday table = new VTableWithUsersBirthdayImpl();
	private final UniformSystemEditorKit editorKid;
	
	public VMTableWithUsersBithdayImpl() {
		this(null);
	}
	
	
	public VMTableWithUsersBithdayImpl(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.initWithEditorKid();
	}

	private void initWithEditorKid()  {
		final ObservableList<UserModel> userModelList = FXCollections.observableArrayList();
		DbDataAll[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUserDataAllDataBase().getUsersBirthdayToday();
		
		for(DbDataAll user : users) {
			if(this.checkUser(user)) {
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
		}
		
		this.table.setData(userModelList);
	}

	@Override
	public VTableWithUsersBirthday getView() {
		return this.table;
	}

	private boolean checkUser(final DbDataAll user) {
		if(user.getCategoryUnit().equals("Рабочий"))
			return false;
		
		return true;
		
	}
	
}
