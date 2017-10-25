package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.PluginsUS;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.NetworkUS;
import ru.kolaer.api.system.ui.UISystemUS;

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
	private Authentication authentication;

	private UniformSystemEditorKitSingleton() {

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

	@Override
	public Authentication getAuthentication() {
		return this.authentication;
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

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
}
