package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PTab;

public interface ExplorerTabObresvable {
	void notifyOpenWindow(final PTab tab);
	void notifyCloseWindow(final PTab tab);
	
	void registerObserver(final ExplorerWindowsObserver observer);
	void removeObserver(final ExplorerWindowsObserver observer);
}
