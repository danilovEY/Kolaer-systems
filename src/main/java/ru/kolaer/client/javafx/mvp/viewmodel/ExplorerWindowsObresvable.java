package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PWindow;

public interface ExplorerWindowsObresvable {
	void notifyOpenWindow(final PWindow window);
	void notifyCloseWindow(final PWindow window);
	
	void registerObserver(final ExplorerWindowsObserver observer);
	void removeObserver(final ExplorerWindowsObserver observer);
}
