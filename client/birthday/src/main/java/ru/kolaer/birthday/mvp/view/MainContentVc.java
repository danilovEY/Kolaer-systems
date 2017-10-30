package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.birthday.mvp.presenter.CalendarVc;

/**
 * ViewModel главного окна.
 *
 * @author danilovey
 * @version 0.1
 */
public interface MainContentVc extends BaseView<MainContentVc, Parent> {
	/**Установить таблицу.*/
	void setVMTableWithUsersBirthday(TableWithUsersBirthdayVc vmTable);
	/**Добавить календарь.*/
	void addVMCalendar(CalendarVc calendar);
}
