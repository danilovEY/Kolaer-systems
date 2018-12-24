package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * ViewModel главного окна.
 *
 * @author danilovey
 * @version 0.1
 */
public interface MainContentVc extends BaseView<MainContentVc, Parent> {
	/**Добавить календарь.*/
	void addVMCalendar(CalendarVc calendar);
}
