package ru.kolaer.asmc.ui.javafx.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.asmc.tools.Resources;

import java.io.IOException;
import java.net.URL;

/**
 * Базовый контроллер для FX компонентов.
 * @author Danilov
 * @version 0.1
 */
public abstract class BaseController extends BorderPane implements Initializable {
	private final Logger LOG = LoggerFactory.getLogger(BaseController.class);
	/**
	 * {@linkplain BaseController}
	 * @param urlView Путь к .fxml.
	 */
	public BaseController(final URL pathView) {	
		try {
			final FXMLLoader loader = new FXMLLoader(pathView);
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (final IOException e) {			
			LOG.error("Не найден view: {}", Resources.V_MAIN_FRAME, e);
		}
	}
}
