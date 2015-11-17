package ru.kolaer.asmc.runnable;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.kolaer.asmc.ui.javafx.presenter.PMainFrame;

public class JavaFXLauncher extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		new PMainFrame().start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
