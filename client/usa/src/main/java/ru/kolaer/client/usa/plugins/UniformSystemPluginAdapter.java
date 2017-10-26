package ru.kolaer.client.usa.plugins;


import javafx.scene.Parent;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;
import java.util.function.Consumer;

public class UniformSystemPluginAdapter implements UniformSystemPlugin {
	private UniformSystemEditorKit editorKit;

	@Override
	public void initialization(UniformSystemEditorKit uniformSystemEditorKit) throws Exception {
		this.editorKit = uniformSystemEditorKit;
	}

	@Override
	public URL getIcon() {
		return null;
	}

	@Override
	public Collection<Service> getServices() {
		return null;
	}

	@Override
	public void start() throws Exception {

	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void updatePluginObjects(String s, Object o) {

	}

	@Override
	public Parent getContent() {
		return null;
	}


	@Override
	public void initView(Consumer<UniformSystemPlugin> viewVisit) {

	}
}
