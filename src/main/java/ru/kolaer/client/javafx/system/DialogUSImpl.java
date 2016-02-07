package ru.kolaer.client.javafx.system;

import org.controlsfx.dialog.ProgressDialog;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
	public void showLoadingDialog(final Service<?> service) {
		Platform.runLater(() -> {
			final ProgressDialog dialog = new ProgressDialog(service);
			dialog.showAndWait();
		});
	}

}
