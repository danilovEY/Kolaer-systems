package ru.kolaer.client.javafx.system;

/**
 * Реализация комплекта инструментов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UniformSystemEditorKitImpl implements UniformSystemEditorKit {
	private final NetworkUS network = new NetworkUSImpl();
	private final UISystemUS uiSystem;
	
	public UniformSystemEditorKitImpl(final UISystemUS uiSystem) {
		this.uiSystem = uiSystem;
	}
	
	public UniformSystemEditorKitImpl() {
		this.uiSystem = new UISystemUSImpl();
	}

	@Override
	public NetworkUS getUSNetwork() {
		return this.network;
	}

	@Override
	public UISystemUS getUISystemUS() {
		return this.uiSystem;
	}
	
}
