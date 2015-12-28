package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public interface PPlugin extends ExplorerObresvable {
	VMLabel getVMLabel();
	PWindow getWindow();
	IKolaerPlugin getPlugin();
}
