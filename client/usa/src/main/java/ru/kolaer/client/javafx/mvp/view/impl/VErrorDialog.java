package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.scene.control.Alert.AlertType;

public class VErrorDialog extends VSimpleDialog {
	
	public VErrorDialog() {
		this.dialog.setAlertType(AlertType.ERROR);
	}
	
}
