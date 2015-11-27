package ru.kolaer.asmc.runnable;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.controller.CMainFrame;

/**
 * Лаунчер (старт) программы.
 * @author danilovey
 */
public class JavaFXLauncher extends Application {

	public static void main(String[] args) {
		SettingSingleton.initialization();
		launch(CMainFrame.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	}
}
