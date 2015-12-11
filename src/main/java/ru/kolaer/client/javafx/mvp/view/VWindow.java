package ru.kolaer.client.javafx.mvp.view;

import javafx.stage.Stage;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VWindow {
	void setVisible(boolean visible);
	
	Stage getStage();
	void setStage(Stage stage);
}
