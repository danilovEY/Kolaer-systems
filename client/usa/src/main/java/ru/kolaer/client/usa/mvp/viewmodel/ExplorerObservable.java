package ru.kolaer.client.usa.mvp.viewmodel;

import ru.kolaer.client.usa.services.RemoteActivationDeactivationPlugin;

/**
 * Оповещает обсерверы при открытии/закрытии и добавлении вкладки.
 *
 * @author Danilov
 * @version 0.1
 */
public interface ExplorerObservable {
	/**Оповестить обсерверы об открытии вкладки.*/
	void notifyActivationPlugin(RemoteActivationDeactivationPlugin plugin);
	/**Оповестить обсерверы о закрытии вкладки.*/
	void notifyDeactivationPlugin(RemoteActivationDeactivationPlugin plugin);
	/**Оповестить обсерверы о добавлении вкладки.*/
	void notifyAddPlugin(RemoteActivationDeactivationPlugin plugin);
	
	/**Зарегистрировать обсервер.*/
	void registerObserver(ExplorerObserver observer);
	/**Удалить обсервер.*/
	void removeObserver(ExplorerObserver observer);
}
