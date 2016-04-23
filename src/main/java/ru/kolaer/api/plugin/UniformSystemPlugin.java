package ru.kolaer.api.plugin;

import java.util.List;

import ru.kolaer.api.mvp.view.VComponentUI;
import ru.kolaer.api.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

/**
 * Интерфейс для плагинов.
 * 
 * @author danilovey
 * @version 1.0
 */
public interface UniformSystemPlugin extends VComponentUI {
	/**Инициализация плагина */
	void initialization(UniformSystemEditorKit editorKid) throws Exception;

	/**Получить путь к ресурсу иконки.*/
	String getIcon();

	/**Получить коллекцию служб плагина.*/
	List<Service> getServices();

	/**Запустить работу плагина.*/
	void start() throws Exception;
	/**Остановить плагина.*/
	void stop() throws Exception;

	void updatePluginObjects(String key, Object object);
}
