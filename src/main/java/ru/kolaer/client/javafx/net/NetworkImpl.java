package ru.kolaer.client.javafx.net;

import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

public class NetworkImpl implements Network {
	
	private VMExplorer explorer;
	
	@Override
	public void run() {
		
	}

	@Override
	public void registerExplorerListener(VMExplorer explorer) {
		this.explorer = null;
		this.explorer = explorer;
	}

	@Override
	public VMExplorer getRegisterExplorerListener() {
		return this.explorer;
	}

}
