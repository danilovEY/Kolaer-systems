package com.teamdev.jxbrowser.chromium.demo;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import com.teamdev.jxbrowser.chromium.javafx.DefaultPopupHandler;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
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
        
        browser.setPopupHandler(new DefaultPopupHandler());
        browser.setDownloadHandler(download -> {
        	final CountDownLatch doneLatch = new CountDownLatch(1);
        	
        	Platform.runLater(() -> {
        		final String urlFile = download.getDestinationFile().getAbsolutePath();
        		final String type = urlFile.substring(urlFile.lastIndexOf('.'));
        		
        		final FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(type, type));
				final Optional<File> file =  Optional.ofNullable(fileChooser.showSaveDialog(this.dialog));
				download.setDestinationFile(file.get());
				doneLatch.countDown();
        	});	
        	
        	try{
				doneLatch.await();
			}
			catch(Exception e){
				return false;
			}
        	
        	return true;
        });
        
        final StackPane pane = new StackPane();
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
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		
		this.dialog.setOnCloseRequest(e -> {
			final ExecutorService thread = Executors.newSingleThreadExecutor();
			thread.submit(() -> {
				this.browser.stop();
				this.browser.dispose();
			});
			thread.shutdown();
			this.dialog.close();
		});
		
		this.dialog.show();
	}    
}
