package ru.kolaer.client.javafx.mvp.presenter.impl;

import ru.kolaer.client.javafx.mvp.presenter.PDialog;
import ru.kolaer.client.javafx.mvp.view.VDialog;
import ru.kolaer.client.javafx.mvp.view.impl.VSimpleDialog;

public class PDialogImpl implements PDialog {
	private VDialog view = new VSimpleDialog();
	
	public PDialogImpl() {
		
	}
	
	@Override
	public void setView(final VDialog view) {
		this.view = view;
	}

	@Override
	public VDialog getDialog() {
		return this.view;
	}

	@Override
	public void show() {
		this.view.show(false);
	}

	@Override
	public void showDialog() {
		this.view.show(true);
	}

	@Override
	public void close() {
		this.view.close();
	}

	@Override
	public void setTitle(final String title) {
		this.view.setTitle(title);
	}

	@Override
	public String getTitle() {
		return this.view.getTitle();
	}

	@Override
	public void setText(final String text) {
		this.view.setText(text);
	}

	@Override
	public String getText() {
		return this.view.getText();
	}
}