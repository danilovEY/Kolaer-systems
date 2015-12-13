package ru.kolaer.client.javafx.mvp.presenter;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PCustomWindow {
	VCustomWindow getView();
	
	Pane getParent();
	void setParent(Pane parent);
	
	void show();
	void close();
	
	void maximize();
	void minimize();
}
