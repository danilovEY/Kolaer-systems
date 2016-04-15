package ru.kolaer.api.plugin;

import javafx.scene.layout.Pane;

/**
 * Интерфейс для содержимого(контента) плагина.
 * @author danilovey
 * @version 0.1
 */
public interface UniformSystemApplication {
	/**Получить путь к ресурсу иконки.*/
	String getIcon();
	/**Получить контент с плагином.*/
	Pane getContent();
	/**Получить имя.*/
	String getName();
	/**Запустить работу плагина.*/
	void start() throws Exception;
	/**Остановить плагина.*/
	void stop() throws Exception;
}
