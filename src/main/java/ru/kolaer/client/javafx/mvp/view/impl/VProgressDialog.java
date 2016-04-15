package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.application.Platform;
import javafx.concurrent.Service;
import org.controlsfx.dialog.ProgressDialog;
import ru.kolaer.api.mvp.view.VDialog;

public class VProgressDialog implements VDialog {
	private final Service<?> service;
	private ProgressDialog dialog;
	
	public VProgressDialog(final Service<?> service) {
		this.service = service;
		Platform.runLater(() -> {
			this.dialog = new ProgressDialog(service);
		});
	}

	@Override
	public void setText(final String text) {
		Platform.runLater(() -> {
			this.setText(text);
		});
	}

	@Override
	public String getText() {
		return this.dialog.getContentText();
	}

	@Override
	public void show(boolean isDialog) {
		Platform.runLater(() -> {
			if(!service.isRunning())
				service.start();
			if(isDialog) {
				this.dialog.showAndWait();
			} else {
				this.dialog.show();
			}
		});
	}

	@Override
	public void setTitle(final String title) {
		Platform.runLater(() -> {
			this.dialog.setTitle(title);
		});
	}

	@Override
	public String getTitle() {
		return this.dialog.getTitle();
	}

	@Override
	public void close() {
		Platform.runLater(() -> {
			if(service.isRunning())
				service.cancel();
			
			this.dialog.close();
		});
	}

}
