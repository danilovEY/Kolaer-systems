package ru.kolaer.birthday.mvp.presenter;

import ru.kolaer.birthday.mvp.model.UserModel;

import java.util.List;

public interface ObservableSearch {
	void addObserver(ObserverSearch observer);
	void removeObserver(ObserverSearch observer);
	void notifySearchUsers(List<UserModel> users);
}
