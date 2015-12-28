package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PWindow;

public interface ExplorerObresvable {
	void notifyOpenWindow(final PWindow window);
	void notifyCloseWindow(final PWindow window);
	
	void registerObserver(final ExplorerObserver observer);
	void removeObserver(final ExplorerObserver observer);
}
