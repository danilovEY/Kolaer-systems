/*
 * Copyright (c) 2000-2015 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.chromium.demo;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;

public class JxBrowserDemo{
	
	private final Stage dialog = new Stage();
    private final Scene scene;
    private final Browser browser = new Browser();
    
    public JxBrowserDemo() {   	
        BrowserView browserView = new BrowserView(browser);

        browser.addTitleListener(event -> {
			Platform.runLater(() -> {
				dialog.setTitle(event.getTitle());
			});				
		});
        
        StackPane pane = new StackPane();
        pane.getChildren().add(browserView);
        this.scene = new Scene(pane, 700, 500);
	}
    
    public void load(String url) {
    	browser.loadURL(url);
    }
    
    public void show() {
		this.dialog.setTitle("Загрузка...");
		this.dialog.setScene(this.scene);
		this.dialog.setMaximized(true);
		this.dialog.centerOnScreen();
		
		try {
			this.dialog.getIcons().add(new Image("file:"+Resources.AER_LOGO.toString()));
		} catch(IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		
		this.dialog.show();
	}
}
