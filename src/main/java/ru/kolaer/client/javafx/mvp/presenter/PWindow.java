package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PWindow {
	VWindow getView();
	void setVWindow(VWindow window);
	
	void show();
	void close();
}
