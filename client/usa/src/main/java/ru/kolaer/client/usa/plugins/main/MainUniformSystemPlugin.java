package ru.kolaer.client.usa.plugins.main;

import ru.kolaer.client.usa.plugins.UniformSystemPluginAdapter;

/**
 * Presenter главного окна. Для регистрации в службе {@linkplain ServiceRemoteActivOrDeactivPlugin}.
 * @author Danilov
 * @version 0.1
 */
public class MainUniformSystemPlugin extends UniformSystemPluginAdapter {
	@Override
	public void stop() {
		System.exit(-1);
	}
}
