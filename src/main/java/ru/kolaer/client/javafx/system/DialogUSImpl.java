package ru.kolaer.client.javafx.system;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogUSImpl implements DialogUS {

	@Override
	public void showSimpleDialog(String title, String text) {
		Platform.runLater(() -> {
			Alert dialog = new Alert(AlertType.NONE);
			dialog.setContentText(text);
			dialog.setTitle(title);
			dialog.show();
		});
	}

}
