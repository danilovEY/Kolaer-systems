package ru.kolaer.asmc.runnable;

import javafx.application.Application;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.controller.CMainFrame;

/**
 * Лаунчер (старт) программы.
 * @author danilovey
 */
public class JavaFXLauncher {

	public static void main(String[] args) {
		SettingSingleton.initialization();
		Application.launch(CMainFrame.class, args);
	}
}
