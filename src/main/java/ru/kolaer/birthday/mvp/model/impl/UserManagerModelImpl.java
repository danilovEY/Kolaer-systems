package ru.kolaer.birthday.mvp.model.impl;

import java.util.LinkedList;
import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.UserManagerModel;

public class UserManagerModelImpl implements UserManagerModel {
	final List<UserModel> usersTodayBithday = new LinkedList<>();
	
	
	public UserManagerModelImpl() {
		
	}

	@Override
	public List<UserModel> getUsersTodayBithday() {
		return null;
	}
	
	
}
