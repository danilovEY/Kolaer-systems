package ru.kolaer.client.javafx.services;

/**
 * Интерфейс для удаленной активации/дезактивации плагина.
 * @author Danilov
 * @version 0.1
 */
public interface RemoteActivationDeactivationPlugin {
	/**Активация/загрузка плагина.*/
	void activation();
	/**Дезактивация плагина.*/
	void deactivation();
	/**Имя плагина.*/
	String getName();
}
