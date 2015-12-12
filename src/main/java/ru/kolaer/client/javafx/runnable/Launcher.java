package ru.kolaer.client.javafx.runnable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;

public class Launcher extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.runLater(() -> {
			VMMainFrameImpl vFrame = new VMMainFrameImpl();
			vFrame.start(primaryStage);
		});
	}
}
