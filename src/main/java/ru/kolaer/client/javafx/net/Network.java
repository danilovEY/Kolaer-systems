package ru.kolaer.client.javafx.net;

import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

public interface Network extends Runnable {
	void registerExplorerListener(VMExplorer explorer);
	VMExplorer getRegisterExplorerListener();
}
