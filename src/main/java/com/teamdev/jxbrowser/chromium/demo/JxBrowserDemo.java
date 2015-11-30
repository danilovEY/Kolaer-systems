package com.teamdev.jxbrowser.chromium.demo;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.EditorCommand;
import com.teamdev.jxbrowser.chromium.KeyFilter;
import com.teamdev.jxbrowser.chromium.PrintJob;
import com.teamdev.jxbrowser.chromium.PrintSettings;
import com.teamdev.jxbrowser.chromium.PrintStatus;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Application;
import ru.kolaer.asmc.tools.Resources;

public class JxBrowserDemo{
	
	private final Stage dialog = new Stage();
    private final Scene scene;
    private final Browser browser = new Browser();
    
    public JxBrowserDemo() {  
    	final BrowserView browserView = new BrowserView(browser);
    	
    	final ContextMenu contextBrowser = new ContextMenu();
		final MenuItem contextBrowserCopy = new MenuItem(Resources.MENU_ITEM_BROWSER_COPY);
		final MenuItem contextBrowserPlase = new MenuItem(Resources.MENU_ITEM_BROWSER_PLASE);
		contextBrowser.getItems().addAll(contextBrowserCopy,contextBrowserPlase);	
		
		
		contextBrowserPlase.setOnAction(e -> {
			final Clipboard clipboard = Clipboard.getSystemClipboard();
			browser.executeCommand(EditorCommand.INSERT_TEXT, clipboard.getString());	 
		});
		
		browserView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			Platform.runLater(() -> {
				contextBrowser.hide();
			});
		});

        browserView.setKeyFilter(event -> {
        	if(event.isControlDown() && event.getCode() == KeyCode.P){
        		browserView.focus();
        		browser.getPrintHandler().onPrint(null);
        	}
            return event.isControlDown() && event.getCode() == KeyCode.P;
        });
		
		browserView.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
			browser.setContextMenuHandler(context -> {	
	    		Platform.runLater(() -> {				
					if(context.getSelectionText().isEmpty()) {
						contextBrowserCopy.setVisible(false);
					} else {
						contextBrowserCopy.setVisible(true);
					}
					
					contextBrowserCopy.setOnAction(e -> {
					     final Clipboard clipboard = Clipboard.getSystemClipboard();
					     final ClipboardContent content = new ClipboardContent();
					     content.putString(context.getSelectionText());
					     clipboard.setContent(content);
					});
				
					contextBrowser.show(browserView, event.getScreenX(), event.getScreenY());
				});
			});
		});

		browser.setPrintHandler(print -> {			
			browser.printToPDF("C:\\Temp\\web_page_pring.pdf");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Application("C:\\Temp\\web_page_pring.pdf", "").start();
			return PrintStatus.CANCEL;
		});

		
    	browser.setContextMenuHandler(context -> {	
    		Platform.runLater(() -> {
    			if(context.getSelectionText().isEmpty()) {
					contextBrowserCopy.setVisible(false);
				} else {
					contextBrowserCopy.setVisible(true);
				}
				
				contextBrowserCopy.setOnAction(e -> {
				     final Clipboard clipboard = Clipboard.getSystemClipboard();
				     final ClipboardContent content = new ClipboardContent();
				     content.putString(context.getSelectionText());
				     clipboard.setContent(content);
				});
			
				contextBrowser.show(browserView, context.getLocation().getX(), context.getLocation().getY());
			});
		});

        browser.addTitleListener(event -> {
			Platform.runLater(() -> {
				dialog.setTitle(event.getTitle());
			});				
		});
        
        browser.setPopupHandler(pop -> {
        	return (br, r) -> {
        		Platform.runLater(() -> {
	        		BrowserView brView = new BrowserView(br);
	        		final StackPane pane = new StackPane();
	                pane.getChildren().add(brView);
	                Scene scene = new Scene(pane, 700, 500);
	                Stage dialog = new Stage();
	                dialog.setTitle("Загрузка...");
	        		dialog.setScene(scene);
	        		dialog.centerOnScreen();
	        		
	        		try {
	        			dialog.getIcons().add(new Image("file:"+Resources.AER_LOGO.toString()));
	        		} catch(IllegalArgumentException e) {
	        			final Alert alert = new Alert(AlertType.ERROR);
	        			alert.setTitle("Ошибка!");
	        			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
	        			alert.showAndWait();
	        		}
	        		
	        		dialog.setOnCloseRequest(e -> {
	        			final ExecutorService thread = Executors.newSingleThreadExecutor();
	        			thread.submit(() -> {
	        				br.stop();
	        				br.dispose();
	        			});
	        			thread.shutdown();
	        			dialog.close();
	        		});
	        		
	        		br.setDownloadHandler(this.browser.getDownloadHandler());
        			br.setPopupHandler(this.browser.getPopupHandler());
        			br.addTitleListener(e -> dialog.setTitle(e.getTitle()));
	        		
	        		dialog.show();
        		});
        	};
		});
        
        browser.setDownloadHandler(download -> {
        	final CountDownLatch doneLatch = new CountDownLatch(1);
        	
        	Platform.runLater(() -> {
        		final String urlFile = download.getDestinationFile().getAbsolutePath();
        		final String type = urlFile.substring(urlFile.lastIndexOf('.'));
        		
        		final FileChooser fileChooser = new FileChooser();
        		fileChooser.setInitialFileName(download.getDestinationFile().getName());
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
