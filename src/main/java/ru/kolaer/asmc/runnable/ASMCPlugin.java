package ru.kolaer.asmc.runnable;

import java.util.List;

import ru.kolaer.client.javafx.plugins.UniformSystem;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
/**
 * Плагин для единой системы КолАЭР.
 * @author danilovey
 * @version 0.1
 */
@UniformSystem
public class ASMCPlugin implements UniformSystemPlugin {
	/**Приложение.*/
	private ASMCApplication app;
	
	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		this.app = new ASMCApplication(editorKid);
	}

	@Override
	public String getName() {
		return "ASUP";
	}

	@Override
	public UniformSystemApplication getApplication() {
		return this.app;
	}

	@Override
	public List<Service> getServices() {
		return null;
	}

	@Override
	public void updatePluginObjects(String key, Object object) {
		
	}
}
