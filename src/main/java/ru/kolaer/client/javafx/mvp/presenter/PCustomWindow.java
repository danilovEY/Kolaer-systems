package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PCustomWindow {
	VCustomWindow getView();
	void setView(VCustomWindow view);
	
	void show();
	void close();
}
