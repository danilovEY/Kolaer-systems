package ru.kolaer.common.plugins;

import javafx.scene.Node;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.plugins.services.Service;
import ru.kolaer.common.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;

/**
 * Интерфейс для плагинов.
 * 
 * @author danilovey
 * @version 1.0
 */
public interface UniformSystemPlugin extends BaseView<UniformSystemPlugin, Node> {
	/**Инициализация плагина */
	default void initialization(UniformSystemEditorKit editorKit) throws Exception {}

	/**Получить путь к ресурсу иконки.*/
	default URL getIcon() {
		return null;
	}

	/**Получить коллекцию служб плагина.*/
	default Collection<Service> getServices() {
		return Collections.emptyList();
	}

	/**Запустить работу плагина.*/
	default void start() throws Exception {}
	/**Остановить плагина.*/
	default void stop() throws Exception {}

	default void updatePluginObjects(String key, Object object) {}

	default boolean isInitPluginView() {
		return true;
	}

}
