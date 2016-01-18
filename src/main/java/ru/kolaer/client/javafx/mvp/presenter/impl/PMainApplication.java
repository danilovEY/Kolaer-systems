package ru.kolaer.client.javafx.mvp.presenter.impl;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.plugins.IApplication;

public class PMainApplication implements IApplication {

	@Override
	public void run() {
		
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public Pane getContent() {
		return null;
	}

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public void stop() {
		System.exit(-1);
	}

}
