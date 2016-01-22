package ru.kolaer.birthday.mvp.model.impl;

import java.util.LinkedList;
import java.util.List;

import ru.kolaer.birthday.mvp.model.UserManager;
import ru.kolaer.birthday.mvp.model.UserManagerModel;

public class UserManagerModelImpl implements UserManagerModel {
	final List<UserManager> usersTodayBithday = new LinkedList<>();
	
	
	public UserManagerModelImpl() {
		
	}

	@Override
	public List<UserManager> getUsersTodayBithday() {
		return null;
	}
	
	
}
