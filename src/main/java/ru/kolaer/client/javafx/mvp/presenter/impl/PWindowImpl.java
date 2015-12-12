package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.Optional;

import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.view.impl.VWindowsImpl;
import ru.kolaer.client.javafx.plugins.IApplication;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PWindowImpl implements PWindow {

	private VWindow view = new VWindowsImpl();
	private IApplication application;
	
	public PWindowImpl() {
		this(null);
	}
	
	public PWindowImpl(IApplication app) {
		this(app,app.getName());
	}

	public PWindowImpl(IApplication app, String name) {
		this.application = app;
		this.view.setTitle(Optional.ofNullable(name).orElse(""));

	}
	
	@Override
	public void show() {
		this.view.setVisible(true);
	}

	@Override
	public void close() {
		this.view.setVisible(false);
	}

	@Override
	public VWindow getView() {
		return this.view;
	}

	@Override
	public void setVWindow(VWindow view) {
		this.view = view;
	}

}
