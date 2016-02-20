package ru.kolaer.birthday.mvp.presenter;

import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;

public interface PTableWithUsersBirthdayObserver extends ObserverCalendar, ObserverSearch {
	VTableWithUsersBirthday getView();
	void showTodayBirthday();
}
