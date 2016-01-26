package ru.kolaer.birthday.mvp.viewmodel;

public interface VMMainFrame {
	VMTableWithUsersBirthdayObserver getVMTableWithUsersBirthday();
	void setVMTableWithUsersBirthday(VMTableWithUsersBirthdayObserver vmTable);
	
	void addVMCalendar(VMCalendar calendar);
}
