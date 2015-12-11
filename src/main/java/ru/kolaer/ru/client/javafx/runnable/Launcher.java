package ru.kolaer.ru.client.javafx.runnable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.presenter.PMainFrame;
import ru.kolaer.client.javafx.mvp.presenter.impl.PMainFrameImpl;

public class Launcher extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.runLater(() -> {
			final PMainFrame pFrame = new PMainFrameImpl(primaryStage);
			pFrame.show();
		});
	}
}
