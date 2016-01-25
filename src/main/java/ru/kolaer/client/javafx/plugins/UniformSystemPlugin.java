package ru.kolaer.client.javafx.plugins;

import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

@UniformSystem
public interface UniformSystemPlugin {
	void initialization(UniformSystemEditorKit editorKid) throws Exception;
	String getName();
	UniformSystemLabel getLabel();
	UniformSystemApplication getApplication();
}
