package ru.kolaer.birthday.mvp.presenter;

import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;

public interface ObserverSearch {
	void updateUsers(List<UserModel> users);
}
