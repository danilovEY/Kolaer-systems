package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.concurrent.Service;
import org.controlsfx.dialog.ProgressDialog;
import ru.kolaer.api.mvp.view.VDialog;
import ru.kolaer.api.tools.Tools;

public class VProgressDialog implements VDialog {
	private final Service<?> service;
	private ProgressDialog dialog;
	
	public VProgressDialog(final Service<?> service) {
		this.service = service;
		Tools.runOnThreadFX(() -> {
			this.dialog = new ProgressDialog(service);
		});
	}

	@Override
	public void setText(final String text) {
		Tools.runOnThreadFX(() -> {
			this.setText(text);
		});
	}

	@Override
	public String getText() {
		return this.dialog.getContentText();
	}

	@Override
	public void show(boolean isDialog) {
		Tools.runOnThreadFX(() -> {
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
		Tools.runOnThreadFX(() -> {
			this.dialog.setTitle(title);
		});
	}

	@Override
	public String getTitle() {
		return this.dialog.getTitle();
	}

	@Override
	public void close() {
		Tools.runOnThreadFX(() -> {
			if(service.isRunning())
				service.cancel();
			
			this.dialog.close();
		});
	}

}
