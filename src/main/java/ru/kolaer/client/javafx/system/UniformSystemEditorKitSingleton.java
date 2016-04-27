package ru.kolaer.client.javafx.system;

import ru.kolaer.api.system.NetworkUS;
import ru.kolaer.api.system.PluginsUS;
import ru.kolaer.api.system.UISystemUS;
import ru.kolaer.api.system.UniformSystemEditorKit;

/**
 * Реализация комплекта инструментов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UniformSystemEditorKitSingleton implements UniformSystemEditorKit {
	private static UniformSystemEditorKitSingleton instance;

	public static UniformSystemEditorKitSingleton getInstance() {
		if(instance == null)
			instance = new UniformSystemEditorKitSingleton();

		return instance;
	}

	private NetworkUS network;
	private UISystemUS uiSystem;
	private PluginsUS pluginsUS;

	private UniformSystemEditorKitSingleton() {
		this.network = new NetworkUSImpl();
	}

	@Override
	public NetworkUS getUSNetwork() {
		return this.network;
	}

	@Override
	public UISystemUS getUISystemUS() {
		return this.uiSystem;
	}

	@Override
	public PluginsUS getPluginsUS() {
		return pluginsUS;
	}

	public void setPluginsUS(final PluginsUS pluginsUS) {
		this.pluginsUS = pluginsUS;
	}

	public void setUSNetwork(final NetworkUS networkUS) {
		this.network = networkUS;
	}

	public void setUISystemUS(final UISystemUS uiSystemUS) {
		this.uiSystem = uiSystemUS;
	}	
}
