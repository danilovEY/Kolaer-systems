package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PWindow;

public interface ExplorerWindowsObserver {
	void updateOpenWindow(final PWindow window);
	void updateCloseWindow(final PWindow window);
}
