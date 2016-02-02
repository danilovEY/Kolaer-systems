package ru.kolaer.client.javafx.system;

import org.controlsfx.dialog.ProgressDialog;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
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
	public ProgressBarObservable showLoadingDialog(String text) {
		final ProgressBarObservable progress = new DefaultProgressBar();
		
		Platform.runLater(() -> {	
			final Stage stage = new Stage();	
			final ProgressIndicator pi = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
			progress.registerObserverProgressBar(value -> {	
				Platform.runLater(() -> {
					if(value > 1){				
						stage.close();
					}
					pi.setProgress(value);
				});
			});
			
			progress.setValue(progress.getValue());
			final Label textLabel = new Label(text);
			textLabel.setStyle("-fx-font-size: 20pt;");
			textLabel.setAlignment(Pos.CENTER);
			
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

			stage.initStyle(StageStyle.UNDECORATED);
			stage.setResizable(false);
			stage.setScene(new Scene(mainPane));
			stage.showAndWait();
		});
		return progress;
	}

	@Override
	public void showLoadingDialog(final Service<?> service) {
		Platform.runLater(() -> {
			final ProgressDialog dialog = new ProgressDialog(service);
			dialog.showAndWait();
		});
	}

}
