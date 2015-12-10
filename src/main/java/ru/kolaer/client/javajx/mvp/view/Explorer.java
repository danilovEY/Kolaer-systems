package ru.kolaer.client.javajx.mvp.view;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ru.kolaer.client.javajx.plugins.Label;

public class Explorer extends BorderPane {
	private final AnchorPane decktop = new AnchorPane();
	private final FlowPane decktopWithLabels = new FlowPane();
	private final BorderPane taskBar = new BorderPane();

	public Explorer() {
		this.init();
	}

	private void init() {
		decktopWithLabels.setOrientation(Orientation.VERTICAL);

		decktop.getChildren().add(decktopWithLabels);
		AnchorPane.setBottomAnchor(decktopWithLabels, 0d);
		AnchorPane.setTopAnchor(decktopWithLabels, 0d);
		AnchorPane.setLeftAnchor(decktopWithLabels, 0d);
		AnchorPane.setRightAnchor(decktopWithLabels, 0d);

		this.setCenter(this.decktop);
		this.setBottom(this.taskBar);
	}

	public void addLabel(final Label label) {
		final Button runnLabel = new Button(label.getName(), label.getIcon());
		runnLabel.setStyle("-fx-background-color: transparent;");
		runnLabel.setOnAction(e -> {
			Platform.runLater(() -> {
				final VWindows window = new VWindows(label.getApplication());

				this.decktop.getChildren().add(window.getWindow());
			});	
		});

		this.decktopWithLabels.getChildren().add(runnLabel);
	}

	public void removeLabel(Label label) {

	}
}
