package ru.kolaer.client.javafx.system;

public class UniformSystemEditorKitImpl implements UniformSystemEditorKit {
	private final NetworkUS network = new NetworkUSImpl();
	private final UISystemUS uiSystem = new UISystemUSImpl();
	
	public UniformSystemEditorKitImpl() {
		// TODO Auto-generated constructor stub
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
