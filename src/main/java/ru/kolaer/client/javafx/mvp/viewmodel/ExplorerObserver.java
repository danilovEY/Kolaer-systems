package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;

/**Слушатель {@linkplain ExplorerObservable}.*/
public interface ExplorerObserver {
	/**Оповещение: открытие вкладки.*/
	void updateActivationPlugin(RemoteActivationDeactivationPlugin plugin);
	/**Оповещение: закрытие вкладки.*/
	void updateDeactivationPlugin(RemoteActivationDeactivationPlugin plugin);
	/**Оповещение: добавление вкладки.*/
	void updateAddPlugin(RemoteActivationDeactivationPlugin plugin);
}
