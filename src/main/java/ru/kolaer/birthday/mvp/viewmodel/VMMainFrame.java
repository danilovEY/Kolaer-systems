package ru.kolaer.birthday.mvp.viewmodel;

import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;

/**
 * ViewModel главного окна.
 *
 * @author danilovey
 * @version 0.1
 */
public interface VMMainFrame {
	/**Получить таблицу.*/
	PTableWithUsersBirthdayObserver getVMTableWithUsersBirthday();
	/**Установить таблицу.*/
	void setVMTableWithUsersBirthday(PTableWithUsersBirthdayObserver vmTable);
	/**Добавить календарь.*/
	void addVMCalendar(PCalendar calendar);
}
