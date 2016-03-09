package ru.kolaer.client.javafx.plugins;

import java.util.List;

import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

public class UniformSystemPluginAdapter implements UniformSystemPlugin {

	@Override
	public void initialization(UniformSystemEditorKit editorKid) throws Exception {
		
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public UniformSystemApplication getApplication() {
		return null;
	}

	@Override
	public List<Service> getServices() {
		return null;
	}

	@Override
	public void updatePluginObjects(String key, Object object) {
		
	}

}
