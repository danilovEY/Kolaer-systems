package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PTab;

public interface ExplorerTabsObserver {
	void updateOpenTab(final PTab tab);
	void updateCloseTab(final PTab tab);
}
