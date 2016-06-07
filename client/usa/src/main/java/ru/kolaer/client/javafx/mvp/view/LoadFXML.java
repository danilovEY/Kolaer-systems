package ru.kolaer.client.javafx.mvp.view;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Загружает .fxml по URL.
 * @author Danilov
 * @version 0.1
 */
public abstract class LoadFXML extends BorderPane implements Initializable {
	private final Logger LOG = LoggerFactory.getLogger(LoadFXML.class);
	/**
	 * {@linkplain LoadFXML}
	 */
	public LoadFXML(final URL pathFXML) {
		try{
			final FXMLLoader loader = new FXMLLoader(pathFXML);
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		}
		catch(final IOException e){
			LOG.error("Не удалось загрузить view \"{}\"!", pathFXML.toString(), e);
		}
	}
}
