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
		this.dialog = new ProgressDialog(service);
	}

	@Override
	public void setText(final String text) {
		Tools.runOnWithOutThreadFX(() -> {
			this.setText(text);
		});
	}

	@Override
	public String getText() {
		return this.dialog.getContentText();
	}

	@Override
	public void show(boolean isDialog) {
		Tools.runOnWithOutThreadFX(() -> {
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
		Tools.runOnWithOutThreadFX(() -> {
			this.dialog.setTitle(title);
		});
	}

	@Override
	public String getTitle() {
		return this.dialog.getTitle();
	}

	@Override
	public void close() {
		Tools.runOnWithOutThreadFX(() -> {
			if(service.isRunning())
				service.cancel();
			
			this.dialog.close();
		});
	}

}
