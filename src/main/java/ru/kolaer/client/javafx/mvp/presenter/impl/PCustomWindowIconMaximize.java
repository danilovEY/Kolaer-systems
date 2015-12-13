package ru.kolaer.client.javafx.mvp.presenter.impl;

import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowIconMaximize extends WindowIcon {
	public static final String DEFAULT_STYLE_CLASS = "window-custom-maximize-icon";
	
	/**
	 * {@linkplain PCustomWindowIconMaximize}
	 */
	public PCustomWindowIconMaximize(PCustomWindow window) {
		this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		this.setOnAction(e -> {
			window.maximize();
		});
	}
}
