package ru.kolaer.client.javafx.plugins.main;

import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class MainRemoteActivDeactivPlugin implements RemoteActivationDeactivationPlugin {
	private final UniformSystemPlugin app = new MainUniformSystemPlugin();

	@Override
	public void activation() {
		
	}

	@Override
	public void deactivation() {
		try{
			this.app.getApplication().stop();
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "Main";
	}
}
