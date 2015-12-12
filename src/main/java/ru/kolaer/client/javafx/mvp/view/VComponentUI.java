package ru.kolaer.client.javafx.mvp.view;

import javafx.scene.layout.Pane;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VComponentUI {
	void setContent(Pane content);
	Pane getContent();
}
