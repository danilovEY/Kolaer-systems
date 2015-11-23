package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;

public class CWebBrowser extends BaseController implements Initializable {
	
	private final Stage dialog = new Stage();
	
	@FXML
	private WebView webBrowser;
	
	public CWebBrowser() {
		super(Resources.V_WEB_BROWSER);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		WebEngine webEngine = webBrowser.getEngine();
		webEngine.getLoadWorker().stateProperty()
        .addListener((obs, oldValue, newValue) -> {
          if (newValue == State.SUCCEEDED) {
        	  this.dialog.setTitle(webEngine.getLocation() + " (Готово)");
          }
        });
	}
	
	public void load(String url) {
		WebEngine webEngine = webBrowser.getEngine();
		webEngine.load(url);
		this.dialog.setTitle(webEngine.getLocation() + " (Загрузка...)");
	}
	
	public void show() {
		this.dialog.setTitle("Загрузка...");
		this.dialog.setMaximized(true);
		this.dialog.setScene(new Scene(this));
		this.dialog.centerOnScreen();
		this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		this.dialog.show();
	}
}
