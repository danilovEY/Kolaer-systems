package ru.kolaer.birthday.mvp.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface VSearchUsers extends View {
	void setSearchAction(EventHandler<ActionEvent> value);
	String getSearchText();
}
