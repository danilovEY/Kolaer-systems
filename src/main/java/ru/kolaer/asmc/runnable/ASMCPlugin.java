package ru.kolaer.asmc.runnable;

import java.util.List;

import ru.kolaer.api.plugin.AbstractUniformSystemPlugin;
import ru.kolaer.api.plugin.UniformSystem;
import ru.kolaer.api.plugin.UniformSystemApplication;
import ru.kolaer.api.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
/**
 * Плагин для единой системы КолАЭР.
 * @author danilovey
 * @version 0.1
 */
@UniformSystem
public class ASMCPlugin extends AbstractUniformSystemPlugin {
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
