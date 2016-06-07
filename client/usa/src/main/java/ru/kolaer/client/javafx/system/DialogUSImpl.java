package ru.kolaer.client.javafx.system;

import javafx.concurrent.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import org.controlsfx.dialog.LoginDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.DialogUS;

/**
 * Реализация диалоговых окон.
 *
 * @author danilovey
 * @version 0.1
 */
public class DialogUSImpl implements DialogUS {
	private final Logger LOG = LoggerFactory.getLogger(DialogUSImpl.class);

	@Override
	public Dialog<?> createSimpleDialog(final String title, final String text) {
		 final Alert dlg = new Alert(AlertType.NONE);
         dlg.setTitle(title);
         dlg.setContentText(text);
         return dlg;
	}

	@Override
	public Dialog<?> createErrorDialog(final String title, final String text) {
		final Alert dlg = new Alert(AlertType.ERROR);
        dlg.setTitle(title);
        dlg.setContentText(text);
        return dlg;
	}

	@Override
	public Dialog<?> createLoadingDialog(final Service<?> service) {
         final ProgressDialog dlg = new ProgressDialog(service);

         service.start();
         return dlg;
	}

	@Override
	public Dialog<?> createInfoDialog(String title, String text) {
		final Alert dlg = new Alert(AlertType.INFORMATION);
        dlg.setTitle(title);
        dlg.setContentText(text);
        return dlg;
	}

	@Override
	public Dialog<?> createLoginDialog() {
		final LoginDialog dlg = new LoginDialog(null, null);
		return dlg;
	}
}
