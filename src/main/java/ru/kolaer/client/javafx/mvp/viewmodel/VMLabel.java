package ru.kolaer.client.javafx.mvp.viewmodel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ru.kolaer.client.javafx.mvp.view.VComponentUI;
import ru.kolaer.client.javafx.plugins.ILabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VMLabel extends VComponentUI {
	ILabel getModel();
	
	void setOnAction(final EventHandler<ActionEvent> value);
	EventHandler<ActionEvent> getOnAction();
}
