package ru.kolaer.birthday.mvp.viewmodel;

import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;

public interface VMMainFrame {
	PTableWithUsersBirthdayObserver getVMTableWithUsersBirthday();
	void setVMTableWithUsersBirthday(PTableWithUsersBirthdayObserver vmTable);
	
	void addVMCalendar(PCalendar calendar);
}
