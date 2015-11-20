package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.tools.Resources;

/**
 * Базовый контроллер для FX компонентов.
 * @author Danilov
 * @version 0.1
 */
public abstract class BaseController extends BorderPane implements Initializable {
	
	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
	
	/**
	 * {@linkplain BaseController}
	 * @param urlView Путь к .fxml.
	 */
	public BaseController(String pathView) {
		try {
			FXMLLoader loader = new FXMLLoader(URI.create(pathView).toURL());
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			LOG.error("Не найден view: " + Resources.V_MAIN_FRAME, e);
			System.exit(-9);
		}
	}
}
