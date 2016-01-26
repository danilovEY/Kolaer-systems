package ru.kolaer.birthday.mvp.view;

import javafx.collections.ObservableList;
import ru.kolaer.birthday.mvp.model.UserModel;

public interface VTableWithUsersBithday extends View {
	void setData(ObservableList<UserModel> userList);
}
