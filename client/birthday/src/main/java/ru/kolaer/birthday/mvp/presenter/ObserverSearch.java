package ru.kolaer.birthday.mvp.presenter;

import ru.kolaer.birthday.mvp.model.UserModel;

import java.util.List;

public interface ObserverSearch {
	void updateUsers(List<UserModel> users);
}
