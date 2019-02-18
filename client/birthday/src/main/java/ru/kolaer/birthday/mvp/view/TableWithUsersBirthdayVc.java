package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import ru.kolaer.birthday.mvp.presenter.ObserverCalendar;
import ru.kolaer.birthday.mvp.presenter.ObserverSearch;
import ru.kolaer.client.core.mvp.view.BaseView;
/**
 * View - таблици с данными о днях рождения сотрудников.
 *
 * @author danilovey
 * @version 0.1
 */
public interface TableWithUsersBirthdayVc extends BaseView<TableWithUsersBirthdayVc, Parent>, ObserverCalendar, ObserverSearch {
    void showTodayBirthday();
}
