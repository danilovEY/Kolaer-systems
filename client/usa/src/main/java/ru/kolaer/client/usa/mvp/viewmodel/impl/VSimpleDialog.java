package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.VDialog;
import ru.kolaer.api.tools.Tools;

public class VSimpleDialog implements VDialog {
	private final Logger LOG = LoggerFactory.getLogger(VSimpleDialog.class);
	
	protected Alert dialog;
	
	public VSimpleDialog() {
        this.dialog = new Alert(Alert.AlertType.NONE);
	}
	
	@Override
	public void show(boolean isDialog) {
		Tools.runOnWithOutThreadFX(() -> {
			if(isDialog) {
				this.dialog.showAndWait();
			} else {
				this.dialog.show();
			}
		});
	}

	@Override
	public void close() {
		Tools.runOnWithOutThreadFX(() -> {
			this.dialog.close();
		});
	}

	@Override
	public void setTitle(final String title) {
		Tools.runOnWithOutThreadFX(() -> {
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
		Tools.runOnWithOutThreadFX(() -> {
			this.dialog.setContentText(text);
		});
	}

	@Override
	public String getText() {
		return this.dialog.getContentText();
	}
}