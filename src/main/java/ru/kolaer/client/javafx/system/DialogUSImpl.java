package ru.kolaer.client.javafx.system;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setResizable(false);
			final Label textLabel = new Label(text);
			textLabel.setStyle("-fx-font-size: 17pt;");
			textLabel.setAlignment(Pos.CENTER);
			final ProgressIndicator pi = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
			final HBox mainPane = new HBox(textLabel, pi);	
			mainPane.setAlignment(Pos.CENTER);
			mainPane.setPadding(new Insets(20, 20, 20, 20));
			mainPane.setSpacing(20);
			mainPane.setStyle(
	                "-fx-padding: 5; " + 
	                "-fx-border-width:5; " +
	                "-fx-border-color: " +
	                    "linear-gradient(" +
	                        "to bottom, " +
	                        "#0000FF, " +
	                        "derive(#0099FF, 50%)" +
	                    ");"
	        );
			mainPane.setEffect(new DropShadow());
			stage.setScene(new Scene(mainPane));
			stage.showAndWait();
		});
		return progress;
	}

}
