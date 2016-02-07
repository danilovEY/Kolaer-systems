package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;

public interface ExplorerObresvable {
	void notifyActivationPlugin(final RemoteActivationDeactivationPlugin tab);
	void notifyDeactivationPlugin(final RemoteActivationDeactivationPlugin tab);
	void notifyAddPlugin(final RemoteActivationDeactivationPlugin tab);
	
	void registerObserver(final ExplorerObserver observer);
	void removeObserver(final ExplorerObserver observer);
}
