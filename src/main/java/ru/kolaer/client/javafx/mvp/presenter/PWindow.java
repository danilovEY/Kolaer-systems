package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.plugins.IApplication;

public interface PWindow {
	IApplication getApplicationModel();
	void setApplicationModel(IApplication application);
	
	void show();
	void close();
}
