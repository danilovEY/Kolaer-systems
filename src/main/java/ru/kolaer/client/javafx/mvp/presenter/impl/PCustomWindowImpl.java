package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.Optional;

import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomWindowsImpl;
import ru.kolaer.client.javafx.plugins.IApplication;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowImpl implements PCustomWindow {

	private VCustomWindow view = new VCustomWindowsImpl();
	private IApplication application;
	
	public PCustomWindowImpl() {
		this(null);
	}
	
	public PCustomWindowImpl(IApplication app) {
		this(app,app.getName());
	}

	public PCustomWindowImpl(IApplication app, String name) {
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
	public VCustomWindow getView() {
		return this.view;
	}

	@Override
	public void setView(VCustomWindow view) {
		this.view = view;
	}

}
