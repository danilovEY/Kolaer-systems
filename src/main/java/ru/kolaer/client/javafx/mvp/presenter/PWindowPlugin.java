package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerWindowsObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

/**
 * Presenter для окна плагина.
 * @author Danilov
 * @version 0.1
 * @deprecated Замена на {@linkplain PTab}.
 */
public interface PWindowPlugin extends ExplorerWindowsObresvable {
	/**Получить ярлык плагина.*/
	VMLabel getVMLabel();
	/**Получить пресентер окна.*/
	PWindow getWindow();
	/**Получить плагин.*/
	UniformSystemPlugin getPlugin();
}
