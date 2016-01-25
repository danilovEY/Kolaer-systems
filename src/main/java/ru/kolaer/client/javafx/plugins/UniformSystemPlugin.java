package ru.kolaer.client.javafx.plugins;

import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

@UniformSystem
public interface UniformSystemPlugin {
	void setUniformSystemEditorKit(UniformSystemEditorKit editorKid);
	String getName();
	UniformSystemLabel getLabel();
	UniformSystemApplication getApplication();
}
