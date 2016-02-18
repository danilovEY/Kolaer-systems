package ru.kolaer.client.javafx.system;

/**
 * Реализация комплекта инструментов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UniformSystemEditorKitImpl implements UniformSystemEditorKit {
	private NetworkUS network = new NetworkUSImpl();
	private UISystemUS uiSystem;
	
	public UniformSystemEditorKitImpl(final UISystemUS uiSystem) {
		this.uiSystem = uiSystem;
	}
	
	public UniformSystemEditorKitImpl() {
		this.uiSystem = null;
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
	public void setUSNetwork(NetworkUS networkUS) {
		this.network = networkUS;
	}

	@Override
	public void setUISystemUS(UISystemUS uiSystemUS) {
		this.uiSystem = uiSystemUS;
	}	
}
