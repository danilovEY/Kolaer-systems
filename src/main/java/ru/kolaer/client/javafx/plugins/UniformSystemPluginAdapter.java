package ru.kolaer.client.javafx.plugins;

import ru.kolaer.api.plugin.UniformSystemApplication;
import ru.kolaer.api.plugin.UniformSystemPlugin;
import ru.kolaer.api.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.util.List;

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
