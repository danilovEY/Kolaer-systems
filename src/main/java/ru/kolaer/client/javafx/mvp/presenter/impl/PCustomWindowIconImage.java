package ru.kolaer.client.javafx.mvp.presenter.impl;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowIconImage extends WindowIcon{
	public static final String DEFAULT_STYLE_CLASS = "window-custom-image-icon";
	/**
	 * {@linkplain PCustomWindowIconImage}
	 */
	public PCustomWindowIconImage(PCustomWindow window) {
		
		this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		//TODO проверить на null
		ImageView image = new ImageView(new Image(window.getApplicationModel().getIcon(), true));
		image.setPreserveRatio(false);
		image.setFitHeight(24);
		image.setFitWidth(24);
		this.getChildren().setAll(image);
	}
}
