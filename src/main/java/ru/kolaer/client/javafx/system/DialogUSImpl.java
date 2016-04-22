package ru.kolaer.client.javafx.system;

import javafx.concurrent.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.presenter.PDialog;
import ru.kolaer.api.system.DialogUS;
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
	private final Logger LOG = LoggerFactory.getLogger(DialogUSImpl.class);

	@Override
	public PDialog createSimpleDialog(String title, String text) {
		final PDialog dialog = new PDialogImpl();
		dialog.setText(text);
		dialog.setTitle(title);
		return dialog;
	}

	@Override
	public PDialog createErrorDialog(String title, String text) {
		final PDialog dialog = new PDialogImpl(new VErrorDialog());
		dialog.setText(text);
		dialog.setTitle(title);
		return dialog;
	}

	@Override
	public PDialog createLoadingDialog(final Service<?> service) {
		final PDialog dialog = new PDialogImpl(new VProgressDialog(service));
		dialog.setText("Загрузка");
		dialog.setTitle("Загрузка");
		return dialog;
	}

	@Override
	public PDialog createInfoDialog(String title, String text) {
		final PDialog dialog = new PDialogImpl(new VInfoDialog());
		dialog.setText(text);
		dialog.setTitle(title);
		return dialog;
	}
}
