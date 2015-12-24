package ru.kolaer.client.javafx.mvp.presenter;

import javafx.scene.Parent;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 *  * @deprecated Использовать {@link PCustomStage}.
 */
public interface PCustomWindow extends PWindow {
	VCustomWindow getView();
	
	Parent getParent();
	void setParent(Parent parent);
	
	void maximize();
	void minimize();
}
