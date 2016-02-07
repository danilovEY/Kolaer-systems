package ru.kolaer.client.javafx.system;

import javafx.concurrent.Service;

public interface DialogUS {
	void showSimpleDialog(String title, String text);
	void showErrorDialog(String title, String text);
	void showLoadingDialog(Service<?> service);
}
