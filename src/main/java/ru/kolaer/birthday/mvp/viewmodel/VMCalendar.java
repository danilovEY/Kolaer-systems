package ru.kolaer.birthday.mvp.viewmodel;

import ru.kolaer.birthday.mvp.model.UserManagerModel;
import ru.kolaer.birthday.mvp.view.VCalendar;

public interface VMCalendar {
	VCalendar getView();
	
	void setModel(UserManagerModel model);
	UserManagerModel getModel();
}
