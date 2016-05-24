package com.teamdev.jxbrowser.chromium.demo;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class TabPanel extends BorderPane {
	private final TabPane tabPane;
	
	public TabPanel() {
		this.tabPane = new TabPane();
		this.tabPane.setTabMaxWidth(200);
		this.setCenter(this.tabPane);
	}
	
	public void addTab(final TabWithBrowser tab) {
		Platform.runLater(() -> {
			this.tabPane.getTabs().add(tab.getContent());
			this.tabPane.getSelectionModel().select(tab.getContent());
		});
	}
	
	public int getCountTabs() {
		return this.tabPane.getTabs().size();
	}
	
}
