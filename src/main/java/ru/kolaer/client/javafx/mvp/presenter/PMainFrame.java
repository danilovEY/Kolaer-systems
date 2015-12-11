package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VMainFrame;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PMainFrame extends PWindow {
	VMainFrame getView();
	void setView(VMainFrame view);
	
}
