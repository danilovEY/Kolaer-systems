package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VLabel;
import ru.kolaer.client.javafx.plugins.ILabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PLabel {
	VLabel getView();
	void setView(VLabel view);
	
	ILabel getModel();
	void setModel(ILabel model);
	
}
