package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;

/**
 * Окно о программе.
 *
 * @author danilovey
 * @version 0.1
 */
public class CAbout extends BaseController implements Initializable{
	private final Logger LOG = LoggerFactory.getLogger(CAbout.class);
	
	@FXML
	private Label labelVersion;
	
	public CAbout() {
		super(Resources.V_ABOUT);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		labelVersion.setText(Resources.VERSION);
	}
	
	public void show() {
		final Stage dialog = new Stage();
		dialog.setTitle("О программе");
		dialog.setScene(new Scene(this));
		dialog.setResizable(false);
		dialog.centerOnScreen();
		dialog.setOnCloseRequest(e -> {
			dialog.close();
		});
		
		try {
			dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		} catch (IllegalArgumentException e) {
			LOG.error("Не найден файл: {}", Resources.AER_LOGO);
		}

		dialog.showAndWait();
	}
}