package ru.kolaer.client.javafx.services;

/**
 * Интерфейс для удаленной активации/дезактивации плагина.
 * @author Danilov
 * @version 0.1
 */
public interface RemoteActivationDeactivationPlugin {
	void activation();
	void deactivation();
	String getName();
}
