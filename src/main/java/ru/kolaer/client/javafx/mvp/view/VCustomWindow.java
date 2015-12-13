package ru.kolaer.client.javafx.mvp.view;

import javafx.scene.control.Control;
import jfxtras.labs.scene.control.window.WindowIcon;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VCustomWindow extends VWindow {
	Control getWindow();
	
	void addRightWindowIcon(WindowIcon icon);
	void addLeftWindowIcon(WindowIcon icon);
	
}
