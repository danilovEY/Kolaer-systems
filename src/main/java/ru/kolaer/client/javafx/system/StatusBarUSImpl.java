package ru.kolaer.client.javafx.system;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBarUSImpl implements StatusBarUS {
	private final HBox statusBar;
	private final LinkedList<Node> nodes = new LinkedList<>();
	
	public StatusBarUSImpl(final HBox statusBar) {
		this.statusBar = statusBar;
		this.startTask();
	}

	@Override
	public void addProgressBar(ProgressBarObserver progressBar) {
		

	}
	
	private void startTask() {
		if(this.statusBar == null)
			return;
		
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			while(true) {
				TimeUnit.SECONDS.sleep(10);
				if(!this.nodes.isEmpty()) {
					this.nodes.removeFirst();
					Platform.runLater(() -> {
						this.statusBar.getChildren().setAll(this.nodes);
					});
				}
			}
		});
		thread.shutdown();
	}
	
	@Override
	public void addMessage(String message) {
		if(this.statusBar == null)
			return;
		
		Platform.runLater(() -> {
			final Label label = new Label(message);
			label.setStyle("-fx-background-color: #00FFFF");
			this.nodes.add(label);
			this.statusBar.getChildren().setAll(nodes);
		});
	}

}
