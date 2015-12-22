package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.IApplication;

public interface PWindow {
	VWindow getView();
	IApplication getApplicationModel();	
	VMApplicationOnTaskPane getTaskPane();
	
	void show();
	void close();
}
