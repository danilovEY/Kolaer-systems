package ru.kolaer.client.core.plugins.services;

/**Интерфейс службы.*/
public interface Service extends Runnable {
	/**Получить статус службы.*/
	boolean isRunning();
	/**Получить имя.*/
	String getName();
	/**Остановиь службу.*/
	void stop();
}
