package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.view.VExplorer;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class VExplorerImpl extends BorderPane implements VExplorer {
	private final AnchorPane decktop = new AnchorPane();
	private final FlowPane decktopWithLabels = new FlowPane();
	private final BorderPane taskBar = new BorderPane();
	
	
	public VExplorerImpl() {
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

	@Override
	public void updataAddPlugin(IKolaerPlugin plugin) {
		final Button runnLabel = new Button(plugin.getLabel().getName(), plugin.getLabel().getIcon());
		runnLabel.setStyle("-fx-background-color: transparent;");
		runnLabel.setOnAction(e -> {
			Platform.runLater(() -> {
				final VWindowsImpl window = new VWindowsImpl(plugin.getApplication());

				this.decktop.getChildren().add(window.getWindow());
			});	
		});
		
		Platform.runLater(() -> {
			this.decktopWithLabels.getChildren().add(runnLabel);
		});	
	}

	@Override
	public void updataRemovePlugin(IKolaerPlugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Pane getContent() {
		return this;
	}
}
