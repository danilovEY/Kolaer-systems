package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PTab;

public interface ExplorerTabsObresvable {
	void notifyOpenTab(final PTab tab);
	void notifyCloseTab(final PTab tab);
	void notifyAddTab(final PTab tab);
	
	void registerObserver(final ExplorerTabsObserver observer);
	void removeObserver(final ExplorerTabsObserver observer);
}
