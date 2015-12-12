package ru.kolaer.client.javafx.mvp.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ru.kolaer.client.javafx.plugins.ILabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VLabel extends VComponentUI {
	void updateLable(ILabel label);
	
	void setOnAction(EventHandler<ActionEvent> value);
}
