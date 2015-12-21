package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.IApplication;

public interface PWindow {
	IApplication getApplicationModel();
	void setApplicationModel(IApplication application);
	
	VMApplicationOnTaskPane getTaskPane();
	
	void show();
	void close();
}
