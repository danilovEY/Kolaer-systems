package ru.kolaer.client.javafx.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

/**
 * Реализация статус бара.
 *
 * @author danilovey
 * @version 0.1
 */
public class StatusBarUSImpl implements StatusBarUS {
	/**Панель с контентом.*/
	private final HBox statusBar;

	public StatusBarUSImpl(final HBox statusBar) {
		this.statusBar = statusBar;
		this.startTask();
	}

	@Override
	public void addProgressBar(final ProgressBarObservable progressBar) {
		if(this.statusBar == null)
			return;
		final ProgressBar prog = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
		progressBar.registerObserverProgressBar(value -> {
			if(value > 1) {
				Platform.runLater(() -> {
					this.statusBar.getChildren().remove(prog);
				});
			}
		});
		
		Platform.runLater(() -> {		
			this.statusBar.getChildren().add(prog);
		});
	}
	
	/**Автоматическое удаление элементов с панели.*/
	private void startTask() {
		if(this.statusBar == null)
			return;
		
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			while(true) {
				TimeUnit.SECONDS.sleep(15);
				Platform.runLater(() -> {
					if(!this.statusBar.getChildren().isEmpty() && this.statusBar.getChildren().size() > 10) {
						this.statusBar.getChildren().remove(0);
					}
				});
			}
		});
		thread.shutdown();
	}
	
	@Override
	public void addMessage(final String message) {
		if(this.statusBar == null)
			return;
		
		Platform.runLater(() -> {
			final Label label = new Label(message);
			label.setStyle("-fx-background-color: #00FFFF");
			this.statusBar.getChildren().add(label);
		});
	}

}
