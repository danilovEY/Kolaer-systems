package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.view.VComponentUI;

public interface VMStartPane extends VComponentUI {
	boolean isShow();
	void show();
	void close();
}
