package ru.kolaer.client.javafx.system;

public interface UniformSystemEditorKit {
	NetworkUS getUSNetwork();
	UISystemUS getUISystemUS();
	
	void setUSNetwork(NetworkUS networkUS);
	void setUISystemUS(UISystemUS uiSystemUS);
}
