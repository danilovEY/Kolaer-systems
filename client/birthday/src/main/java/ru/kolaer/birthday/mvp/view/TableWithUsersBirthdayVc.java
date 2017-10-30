package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.presenter.ObserverCalendar;
import ru.kolaer.birthday.mvp.presenter.ObserverSearch;

import java.util.List;
/**
 * View - таблици с данными о днях рождения сотрудников.
 *
 * @author danilovey
 * @version 0.1
 */
public interface TableWithUsersBirthdayVc extends BaseView<TableWithUsersBirthdayVc, Parent>, ObserverCalendar, ObserverSearch {
	/**Задать список сотрудников.*/
	void setData(List<UserModel> userList);
	void setTitle(String text);
	void clear();
}
