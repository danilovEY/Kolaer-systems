package ru.kolaer.client.javafx.plugins;

import java.util.List;

import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

public interface UniformSystemPlugin {
	void initialization(UniformSystemEditorKit editorKid) throws Exception;
	String getName();
	UniformSystemApplication getApplication();
	List<Service> getServices();
}
