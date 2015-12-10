package ru.kolaer.ru.client.javajx.runnable;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.kolaer.client.javajx.mvp.view.VMainFrame;

public class Launcher extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new VMainFrame().start(primaryStage);
	}
}
