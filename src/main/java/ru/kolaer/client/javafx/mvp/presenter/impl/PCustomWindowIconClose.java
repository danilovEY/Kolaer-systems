package ru.kolaer.client.javafx.mvp.presenter.impl;

import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowIconClose extends WindowIcon {
    public static final String DEFAULT_STYLE_CLASS = "window-close-icon";
	/**
	 * {@linkplain PCustomWindowIconClose}
	 */
	public PCustomWindowIconClose(final PCustomWindow window) {
		this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		
		this.setOnAction(e -> {
			window.close();
		});
	}
}
