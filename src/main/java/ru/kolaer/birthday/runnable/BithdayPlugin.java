package ru.kolaer.birthday.runnable;

import java.util.List;

import ru.kolaer.client.javafx.plugins.UniformSystem;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemLabel;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

@UniformSystem
public class BithdayPlugin implements UniformSystemPlugin {
	private UniformSystemApplication application;
	
	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		this.application = new BithdayApplication(editorKid);
	}

	@Override
	public String getName() {
		return "Bithday";
	}

	@Override
	public UniformSystemLabel getLabel() {
		return null;
	}

	@Override
	public UniformSystemApplication getApplication() {
		return this.application;
	}

	@Override
	public List<Service> getServices() {
		return null;
	}

}
