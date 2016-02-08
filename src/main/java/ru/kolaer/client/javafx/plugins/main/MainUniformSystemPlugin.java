package ru.kolaer.client.javafx.plugins.main;

import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemApplicationAdapter;
import ru.kolaer.client.javafx.plugins.UniformSystemPluginAdapter;
import ru.kolaer.client.javafx.services.ServiceRemoteActivOrDeactivPlugin;

/**
 * Presenter главного окна. Для регистрации в службе {@linkplain ServiceRemoteActivOrDeactivPlugin}.
 * @author Danilov
 * @version 0.1
 */
public class MainUniformSystemPlugin extends UniformSystemPluginAdapter {
	/**Модель имплеминтируищая метод для завешения приложения.*/
	private final UniformSystemApplicationAdapter app = new UniformSystemApplicationAdapter() {
		@Override
		public void stop() {
			System.exit(-1);
		}
		
		@Override
		public String getName() {
			return "Main";
		}
	};
	@Override
	public String getName() {
		return app.getName();
	}
	
	@Override
	public UniformSystemApplication getApplication() {
		return app;
	}
}
