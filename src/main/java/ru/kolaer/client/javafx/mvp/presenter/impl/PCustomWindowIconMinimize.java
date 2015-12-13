package ru.kolaer.client.javafx.mvp.presenter.impl;

import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowIconMinimize extends WindowIcon {
	public static final String DEFAULT_STYLE_CLASS = "window-custom-minimize-icon";
	
	/**
	 * {@linkplain PCustomWindowIconMaximize}
	 */
	public PCustomWindowIconMinimize(PCustomWindow window) {
		this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        setOnAction(e -> {
        	window.minimize();
        });
    }
}
