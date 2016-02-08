package ru.kolaer.client.javafx.services;

/**Интерфейс службы.*/
public interface Service extends Runnable {
	/**Установить статус службы.*/
	void setRunningStatus(boolean isRun);
	boolean isRunning();
	/**Получить имя.*/
	String getName();
	/**Остановиь службу.*/
	void stop();
}
