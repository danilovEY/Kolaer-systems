package ru.kolaer.client.javafx.plugins;


import javafx.scene.Parent;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;

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
	public void setContent(Parent parent) {

	}

	@Override
	public Parent getContent() {
		return null;
	}
}
