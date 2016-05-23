package ru.kolaer.birthday.mvp.presenter;

import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;

public interface ObservableSearch {
	void addObserver(ObserverSearch observer);
	void removeObserver(ObserverSearch observer);
	void notifySearchUsers(List<UserModel> users);
}
