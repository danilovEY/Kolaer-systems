package ru.kolaer.api.plugins;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.mvp.view.InitializationView;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;

/**
 * Интерфейс для плагинов.
 * 
 * @author danilovey
 * @version 1.0
 */
public interface UniformSystemPlugin extends BaseView<Parent>, InitializationView<Parent> {

	/**Инициализация плагина */
	void initialization(UniformSystemEditorKit editorKit) throws Exception;

	/**Получить путь к ресурсу иконки.*/
	URL getIcon();

	/**Получить коллекцию служб плагина.*/
	Collection<Service> getServices();

	/**Запустить работу плагина.*/
	void start() throws Exception;
	/**Остановить плагина.*/
	void stop() throws Exception;

	void updatePluginObjects(String key, Object object);

}
