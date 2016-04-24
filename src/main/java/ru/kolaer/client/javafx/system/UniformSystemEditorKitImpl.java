package ru.kolaer.client.javafx.system;

import ru.kolaer.api.system.NetworkUS;
import ru.kolaer.api.system.UISystemUS;
import ru.kolaer.api.system.UniformSystemEditorKit;

/**
 * Реализация комплекта инструментов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UniformSystemEditorKitImpl implements UniformSystemEditorKit {
	private static UniformSystemEditorKit instance;

	public static UniformSystemEditorKit getInstance() {
		if(instance == null)
			instance = new UniformSystemEditorKitImpl();

		return instance;
	}

	private NetworkUS network;
	private UISystemUS uiSystem;

	private UniformSystemEditorKitImpl() {
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
	public void setUSNetwork(final NetworkUS networkUS) {
		this.network = networkUS;
	}

	@Override
	public void setUISystemUS(final UISystemUS uiSystemUS) {
		this.uiSystem = uiSystemUS;
	}	
}
