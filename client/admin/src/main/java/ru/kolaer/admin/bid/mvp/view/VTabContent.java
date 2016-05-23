package ru.kolaer.admin.bid.mvp.view;

import javafx.scene.control.Tab;

public abstract class VTabContent {
	protected final Tab tab;
	
	public VTabContent() {
		this.tab = new Tab();
	}
	
	public Tab getTab() {
		return this.tab;
	}
}
