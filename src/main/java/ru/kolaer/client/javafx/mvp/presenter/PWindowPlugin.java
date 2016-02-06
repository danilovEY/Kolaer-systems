package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerWindowsObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

public interface PWindowPlugin extends ExplorerWindowsObresvable {
	VMLabel getVMLabel();
	PWindow getWindow();
	UniformSystemPlugin getPlugin();
}
