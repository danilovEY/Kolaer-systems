package ru.kolaer.birthday.mvp.view;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import ru.kolaer.birthday.mvp.model.UserModel;
/**
 * View - таблици с данными о днях рождения сотрудников.
 *
 * @author danilovey
 * @version 0.1
 */
public interface VTableWithUsersBirthday extends View {
	/**Задать список сотрудников.*/
	void setData(List<UserModel> userList);
	/**Добавить сотрудников.*/
	void addData(UserModel user);
	void setTitle(String text);
	/**Задать действие на нажатие по сотруднику в таблице.*/
	void setMouseClick(EventHandler<? super MouseEvent> value);
	
	UserModel getSelectModel();
	void clear();
	String getTitle();
}
