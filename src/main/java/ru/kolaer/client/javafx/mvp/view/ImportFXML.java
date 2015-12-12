package ru.kolaer.client.javafx.mvp.view;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public abstract class ImportFXML extends BorderPane implements Initializable {
	private static final Logger LOG = LoggerFactory.getLogger(ImportFXML.class);
	/**
	 * {@linkplain ImportFXML}
	 */
	public ImportFXML(URL pathFXML) {
		try{
			FXMLLoader loader = new FXMLLoader(pathFXML);
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		}
		catch(IOException e){
			LOG.error("Не удалось загрузить: " + pathFXML.toString(), e);
		}
	}
}
