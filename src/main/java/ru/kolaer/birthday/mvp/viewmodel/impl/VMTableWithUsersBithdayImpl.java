package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBithday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBithdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBithday;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.server.dao.entities.DbUsers1c;

public class VMTableWithUsersBithdayImpl implements VMTableWithUsersBithday{
	private final VTableWithUsersBithday table = new VTableWithUsersBithdayImpl();
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
		DbUsers1c[] users = this.editorKid.getUSNetwork().getKolaerDataBase().getUser1cDataBase().getUsersBithdayToday();
		
		for(DbUsers1c user : users) {
			if(this.checkUser(user)) {
				final UserModel userModel = new UserModelImpl();
				userModel.setPersonNumber(user.getPersonNumber().intValue());
				userModel.setFirstName(user.getName());
				userModel.setSecondName(user.getSurname());
				userModel.setThirdName(user.getPatronymic());
				userModel.setBithday(user.getBirthday());
				userModel.setDepartament(user.getDepartament());
				userModelList.add(userModel);
			}
		}
		
		this.table.setData(userModelList);
	}

	@Override
	public VTableWithUsersBithday getView() {
		return this.table;
	}

	private boolean checkUser(final DbUsers1c user) {
		if(user.getPost().contains("слесарь"))
			return false;
		
		return true;
		
	}
	
}
