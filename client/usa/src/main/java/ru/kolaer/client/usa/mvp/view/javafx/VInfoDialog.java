package ru.kolaer.client.usa.mvp.view.javafx;

import javafx.scene.control.Alert.AlertType;

public class VInfoDialog extends VSimpleDialog {

	public VInfoDialog() {
		this.dialog.setAlertType(AlertType.INFORMATION);
	}

}
