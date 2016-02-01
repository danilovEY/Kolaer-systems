package ru.kolaer.client.javafx.system;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class DialogUSImpl implements DialogUS {

	@Override
	public void showSimpleDialog(String title, String text) {
		this.showDialog(AlertType.NONE, title, text);
	}

	@Override
	public void showErrorDialog(String title, String text) {
		this.showDialog(AlertType.ERROR, title, text);
	}
	
	private void showDialog(AlertType type, String title, String text) {
		Platform.runLater(() -> {
			final Alert dialog = new Alert(type);
			dialog.setContentText(text);
			dialog.setTitle(title);
			dialog.show();
		});
	}

	@Override
	public ProgressBar showLoadingDialog(String title, String text) {
		final ProgressBar progress = new DefaultProgressBar();
		Platform.runLater(() -> {
			final Stage stage = new Stage();
			final Label textLabel = new Label(text);
			final ProgressIndicator pi = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
			final FlowPane mainPane = new FlowPane(textLabel, pi);		
			stage.setScene(new Scene(mainPane, 200, 100));
			stage.showAndWait();
		});
		return progress;
	}

}
