package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerWindowsObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;

public interface PWindowPlugin extends PPlugin, ExplorerWindowsObresvable {
	VMLabel getVMLabel();
	PWindow getWindow();
}
