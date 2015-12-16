package ru.kolaer.client.javafx.mvp.presenter;

import javafx.scene.Parent;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PCustomWindow extends PWindow {
	VCustomWindow getView();
	
	Parent getParent();
	void setParent(Parent parent);
	
	VMApplicationOnTaskPane getTask();
	
	void maximize();
	void minimize();
}
