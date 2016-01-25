package ru.kolaer.asmc.runnable;

import ru.kolaer.client.javafx.plugins.UniformSystem;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemLabel;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

@UniformSystem
public class ASMCPlugin implements UniformSystemPlugin{
	private ASMCApplication app;
	
	@Override
	public void initialization(UniformSystemEditorKit editorKid) throws Exception {
		this.app = new ASMCApplication();
	}

	@Override
	public String getName() {
		return "ASUP";
	}

	@Override
	public UniformSystemLabel getLabel() {
		return null;
	}

	@Override
	public UniformSystemApplication getApplication() {
		return this.app;
	}
}
