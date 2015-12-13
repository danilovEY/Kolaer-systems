package ru.kolaer.client.javafx.mvp.presenter;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.IApplication;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PCustomWindow {
	VCustomWindow getView();

	IApplication getApplicationModel();
	void setApplicationModel(IApplication application);
	
	Pane getParent();
	void setParent(Pane parent);
	
	VMApplicationOnTaskPane show();
	void close();
	
	void maximize();
	void minimize();
}
