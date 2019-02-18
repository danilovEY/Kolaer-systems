package ru.kolaer.client.usa.plugins.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.core.plugins.UniformSystemPlugin;
import ru.kolaer.client.usa.services.RemoteActivationDeactivationPlugin;

/**
 * Класс для удаленного закрытия основного приложения.
 * @author Danilov
 * @version 0.1
 */
public class MainRemoteActivDeactivPlugin implements RemoteActivationDeactivationPlugin {
	private final Logger LOG = LoggerFactory.getLogger(MainRemoteActivDeactivPlugin.class);
	private final UniformSystemPlugin app = new MainUniformSystemPlugin();

	@Override
	public void activation() {
		
	}

	@Override
	public void deactivation() {
		try{
			this.app.stop();
		}catch(Exception e){
			LOG.error("Ошибка при закрытии основного приложения!", e);
			System.exit(-9);
		}
	}

	@Override
	public String getName() {
		return "Main";
	}
}
