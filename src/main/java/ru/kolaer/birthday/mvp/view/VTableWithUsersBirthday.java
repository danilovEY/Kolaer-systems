package ru.kolaer.birthday.mvp.view;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import ru.kolaer.birthday.mvp.model.UserModel;

public interface VTableWithUsersBirthday extends View {
	void setData(List<UserModel> userList);
	void addData(UserModel user);
	void setTitle(String text);
	void setMouseClick(EventHandler<? super MouseEvent> value);
}
