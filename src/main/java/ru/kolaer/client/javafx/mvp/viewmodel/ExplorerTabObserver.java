package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.presenter.PTab;

public interface ExplorerTabObserver {
	void updateOpenWindow(final PTab tab);
	void updateCloseWindow(final PTab tab);
}
