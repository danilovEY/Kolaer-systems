package ru.kolaer.birthday.mvp.viewmodel;

import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;

public interface VMTableWithUsersBirthdayObserver extends ObserverCalendar {
	VTableWithUsersBirthday getView();
}
