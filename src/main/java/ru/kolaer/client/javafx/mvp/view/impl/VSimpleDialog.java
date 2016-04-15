package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.VDialog;

import java.util.concurrent.CountDownLatch;

public class VSimpleDialog implements VDialog {
	private final Logger LOG = LoggerFactory.getLogger(VSimpleDialog.class);
	
	protected Alert dialog;
	
	public VSimpleDialog() {
		final CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			this.dialog = new Alert(AlertType.NONE);
			latch.countDown();
		});
		
		try{
			latch.await();
		}catch(final InterruptedException e){
			LOG.error("Ошибка!", e);
			latch.countDown();
		}
	}
	
	@Override
	public void show(boolean isDialog) {
		Platform.runLater(() -> {
			if(isDialog) {
				this.dialog.showAndWait();
			} else {
				this.dialog.show();
			}
		});
	}

	@Override
	public void close() {
		Platform.runLater(() -> {
			this.dialog.close();
		});
	}

	@Override
	public void setTitle(final String title) {
		Platform.runLater(() -> {
			this.dialog.setTitle(title);
			this.dialog.setHeaderText(title);
		});
	}

	@Override
	public String getTitle() {
		return this.dialog.getTitle();
	}

	@Override
	public void setText(final String text) {
		Platform.runLater(() -> {
			this.dialog.setContentText(text);
		});
	}

	@Override
	public String getText() {
		return this.dialog.getContentText();
	}
}