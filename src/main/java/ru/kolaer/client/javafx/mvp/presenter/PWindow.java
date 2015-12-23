package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.IApplication;

public interface PWindow {
	IApplication getApplicationModel();
		
	VMApplicationOnTaskPane getTaskPane();
	void setTaskPane(final VMApplicationOnTaskPane taskPane);
	
	VWindow getView();
	void setView(final VWindow view);
	
	void show();
	void close();
}
