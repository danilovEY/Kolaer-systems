package ru.kolaer.client.javafx.system;

import javafx.concurrent.Service;
import ru.kolaer.client.javafx.mvp.presenter.PDialog;
import ru.kolaer.client.javafx.mvp.presenter.impl.PDialogImpl;
import ru.kolaer.client.javafx.mvp.view.impl.VErrorDialog;
import ru.kolaer.client.javafx.mvp.view.impl.VInfoDialog;
import ru.kolaer.client.javafx.mvp.view.impl.VProgressDialog;

/**
 * Реализация диалоговых окон.
 *
 * @author danilovey
 * @version 0.1
 */
public class DialogUSImpl implements DialogUS {

	@Override
	public PDialog showSimpleDialog(String title, String text) {
		final PDialog dialog = new PDialogImpl();
		dialog.setText(text);
		dialog.setTitle(title);
		return dialog;
	}

	@Override
	public PDialog showErrorDialog(String title, String text) {
		final PDialog dialog = new PDialogImpl();
		dialog.setView(new VErrorDialog());
		dialog.setText(text);
		dialog.setTitle(title);
		return dialog;
	}

	@Override
	public PDialog showLoadingDialog(final Service<?> service) {		
		final PDialog dialog = new PDialogImpl();
		dialog.setView(new VProgressDialog(service));
		dialog.setText("Загрузка");
		dialog.setTitle("Загрузка");
		return dialog;
	}

	@Override
	public PDialog showInfoDialog(String title, String text) {
		final PDialog dialog = new PDialogImpl();
		dialog.setView(new VInfoDialog());
		dialog.setText(text);
		dialog.setTitle(title);
		return dialog;
	}
}
