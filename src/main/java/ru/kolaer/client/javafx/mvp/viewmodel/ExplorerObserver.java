package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;

public interface ExplorerObserver {
	void updateActivationPlugin(final RemoteActivationDeactivationPlugin tab);
	void updateDeactivationPlugin(final RemoteActivationDeactivationPlugin tab);
	void updateAddPlugin(final RemoteActivationDeactivationPlugin tab);
}
