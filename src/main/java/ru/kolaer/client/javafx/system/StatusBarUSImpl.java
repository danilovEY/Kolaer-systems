package ru.kolaer.client.javafx.system;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBarUSImpl implements StatusBarUS {
	private final HBox statusBar;
	private LinkedList<Label> messages = new LinkedList<>();
	
	public StatusBarUSImpl(final HBox statusBar) {
		this.statusBar = statusBar;
		this.startTask();
	}

	@Override
	public void addProgressBar(ProgressBarObservable progressBar) {
		// TODO Auto-generated method stub

	}
	
	private void startTask() {
		if(this.statusBar == null)
			return;
		
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			while(true) {
				TimeUnit.SECONDS.sleep(10);
				if(!this.messages.isEmpty()) {
					this.messages.removeFirst();
					Platform.runLater(() -> {
						this.statusBar.getChildren().setAll(this.messages);
					});
				}
			}
		});
		thread.shutdown();
	}
	
	@Override
	public void addMessage(String message) {
		if(this.statusBar != null) {
			Platform.runLater(() -> {
				final Label label = new Label(message);
				this.messages.add(label);
				this.statusBar.getChildren().setAll(messages);
			});
		}
	}

}
