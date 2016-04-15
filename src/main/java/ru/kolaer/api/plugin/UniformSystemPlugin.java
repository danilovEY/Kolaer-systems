package ru.kolaer.api.plugin;

import java.util.List;

import ru.kolaer.api.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

/**
 * Интерфейс для плагинов. Обязательное наличет аннотации {@linkplain UniformSystem} над классом
 * реализующий этот интерфейс.
 * 
 * @author danilovey
 * @version 0.1
 */
public interface UniformSystemPlugin {
	/**Инициализация плагина */
	void initialization(UniformSystemEditorKit editorKid) throws Exception;
	/**Получить имя плагина.*/
	String getName();
	/**Получить контент плагина.*/
	UniformSystemApplication getApplication();
	/**Получить коллекцию служб плагина.*/
	List<Service> getServices();
	
	void updatePluginObjects(String key, Object object);
}
