package com.teamdev.jxbrowser.chromium.demo;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.EditorCommand;
import com.teamdev.jxbrowser.chromium.PrintStatus;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class TabWithBrowser{
	private final Logger LOG = LoggerFactory.getLogger(TabWithBrowser.class);
	private Tab tab;
	private BorderPane mainPane;
	private Browser browser;
	private final TabPanel tabPanel;
	
	public TabWithBrowser(final TabPanel tabPanel) {
		this.tabPanel = tabPanel;
		this.init();
	}
	
	public TabWithBrowser(final TabPanel tabPanel, final Browser browser) {
		this.tabPanel = tabPanel;
		this.browser = browser;
		this.init();
	}

	public TabWithBrowser(final TabPanel tabPanel, final String url) {
		this.tabPanel = tabPanel;
		this.init();
		this.loadURL(url);
	}
	
	private void init() {
		if(this.browser == null)
			this.browser = new Browser();

		final BrowserView browserView = new BrowserView(browser);
		this.mainPane = new BorderPane(browserView);
		this.mainPane.setTop(new NavigationPanel(browser));
		this.tab = new Tab("Новая вкладка", this.mainPane);
		
		browser.setPrintHandler(print -> {
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Печать страницы");
		        final File pageToPDF = new File("C:\\Temp\\web_page_print_" + System.currentTimeMillis() + ".pdf");
		        
				browser.printToPDF(pageToPDF.getAbsolutePath());
				
				while(pageToPDF.getTotalSpace() == 0){
					continue;
				}
				
				String pathToAcrobat = "\"" + System.getenv("ProgramFiles");
				
				if(System.getProperty("os.arch").contains("64")) {
					pathToAcrobat += " (x86)";
				}
				
				pathToAcrobat += "\\Adobe\\Reader 10.0\\Reader\\AcroRd32.exe\"";
				
				if(!new File(pathToAcrobat).exists()) {
					pathToAcrobat = "\\\\kolaer.local\\asup$\\Reader\\AcroRd32.exe";
				}
	
				LOG.debug("Path Reader: {}", pathToAcrobat);
				
				try{
					Runtime.getRuntime().exec( pathToAcrobat + " /P \"" + pageToPDF.getAbsolutePath() + "\"");
				}catch(final Exception e1){
					LOG.error("Невозможно запустить reader!", e1);
				}
			}, threadPool).exceptionally(t -> {
				LOG.error("Невозможно запустить reader!", t);
				return null;
			});
			
			return PrintStatus.CANCEL;
		});

		this.browser.addLoadListener(new LoadAdapter() {		
			@Override
			public void onStartLoadingFrame(StartLoadingEvent arg0) {
				Platform.runLater(() -> {
					tab.setText("Загрузка...");
				});
			}
			
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent e) {
				Platform.runLater(() -> {
					tab.setText(e.getBrowser().getTitle());
				});
			}
		});
		
		final ContextMenu contextBrowser = new ContextMenu();
		final MenuItem contextBrowserCopy = new MenuItem("Копировать");
		final MenuItem contextBrowserPlase = new MenuItem("Вставить");
		contextBrowser.getItems().addAll(contextBrowserCopy,contextBrowserPlase);	

		contextBrowserPlase.setOnAction(e -> {
			final Clipboard clipboard = Clipboard.getSystemClipboard();
			browser.executeCommand(EditorCommand.INSERT_TEXT, clipboard.getString());	 
		});		
		
		browserView.setKeyFilter(event -> {
        	if(event.isControlDown() && event.getCode() == KeyCode.P){
        		browser.print();
        		//browser.getPrintHandler().onPrint(browser.getPrintHandler().);
        	}
            return event.isControlDown() && event.getCode() == KeyCode.P;
        });
		
		browserView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			Platform.runLater(() -> {
				contextBrowser.hide();
			});
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
		
		browser.setPopupHandler(pop -> {
			return (br, r) -> {
				Platform.runLater(() -> {
					this.tabPanel.addTab(new TabWithBrowser(this.tabPanel, br));
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
					final File file =  fileChooser.showSaveDialog(null);
					
					if(file != null)
						download.setDestinationFile(file);
					
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
		
		this.tab.setOnClosed(e -> {
			this.browser.stop();
			this.browser.dispose();
		});
	}
	
	private void loadURL(final String url) {
		this.browser.loadURL(url);
	}
	
	public Tab getContent() {
		return this.tab;
	}
}
