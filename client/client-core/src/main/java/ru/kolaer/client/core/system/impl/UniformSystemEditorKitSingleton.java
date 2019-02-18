package ru.kolaer.client.core.system.impl;

import ru.kolaer.client.core.system.Authentication;
import ru.kolaer.client.core.system.PluginsUS;
import ru.kolaer.client.core.system.UniformSystemEditorKit;
import ru.kolaer.client.core.system.network.NetworkUS;
import ru.kolaer.client.core.system.ui.UISystemUS;

/**
 * Реализация комплекта инструментов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UniformSystemEditorKitSingleton implements UniformSystemEditorKit {
	private static UniformSystemEditorKit instance;

	public static void setInstance(UniformSystemEditorKit uniformSystemEditorKit) {
		instance = uniformSystemEditorKit;
	}

	public static UniformSystemEditorKit getInstance() {
		if(instance == null)
			setInstance(new DefaultUniformSystemEditorKit());

		return instance;
	}

	private NetworkUS network;
	private UISystemUS uiSystem;
	private PluginsUS pluginsUS;
	private Authentication authentication;

	public UniformSystemEditorKitSingleton() {

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
