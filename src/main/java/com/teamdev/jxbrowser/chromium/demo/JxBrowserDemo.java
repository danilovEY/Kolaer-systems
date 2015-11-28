/*
 * Copyright (c) 2000-2015 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.chromium.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class JxBrowserDemo{
	
	//private final Stage dialog = new Stage();
    //private final Scene scene;
   // private final Browser browser = new Browser();
    
    public JxBrowserDemo() {  
       /* BrowserView browserView = new BrowserView(browser);

        browser.addTitleListener(event -> {
			Platform.runLater(() -> {
				dialog.setTitle(event.getTitle());
			});				
		});
        
        StackPane pane = new StackPane();
        pane.getChildren().add(browserView);
        this.scene = new Scene(pane, 700, 500);*/
	}
    
    public void load(String url) {
    	initAndDisplayUI(url);
    	//browser.loadURL(url);
    }
    
    public void show() {
		/*this.dialog.setTitle("Загрузка...");
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
		
		this.dialog.setOnCloseRequest(e -> {
			this.browser.dispose();
			this.dialog.close();
		});
		
		this.dialog.show();*/
	}

    private static void initAndDisplayUI(String url) {
        final TabbedPane tabbedPane = new TabbedPane();
        insertTab(tabbedPane, TabFactory.createTab(url));
        insertNewTabButton(tabbedPane);

        JFrame frame = new JFrame("JxBrowser Demo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tabbedPane.disposeAllTabs();
            }
        });
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(com.teamdev.jxbrowser.chromium.demo.resources.Resources.getIcon("jxbrowser16x16.png").getImage());
        frame.setVisible(true);
    }

    private static void insertNewTabButton(final TabbedPane tabbedPane) {
        TabButton button = new TabButton(com.teamdev.jxbrowser.chromium.demo.resources.Resources.getIcon("new-tab.png"), "New tab");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                insertTab(tabbedPane, TabFactory.createTab());
            }
        });
        tabbedPane.addTabButton(button);
    }

    private static void insertTab(TabbedPane tabbedPane, Tab tab) {
        tabbedPane.addTab(tab);
        tabbedPane.selectTab(tab);
    }
    
    
}
