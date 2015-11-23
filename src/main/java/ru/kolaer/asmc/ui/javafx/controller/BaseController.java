package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URI;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.tools.Resources;

/**
 * Базовый контроллер для FX компонентов.
 * @author Danilov
 * @version 0.1
 */
public abstract class BaseController extends BorderPane implements Initializable {
	
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
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден view: \""+Resources.V_MAIN_FRAME+"\"");
			alert.showAndWait();
			
			System.exit(-9);
		}
	}
}
