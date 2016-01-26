package ru.kolaer.birthday.mvp.view;

import java.util.List;

import ru.kolaer.birthday.mvp.model.UserModel;

public interface VTableWithUsersBirthday extends View {
	void setData(List<UserModel> userList);
}
