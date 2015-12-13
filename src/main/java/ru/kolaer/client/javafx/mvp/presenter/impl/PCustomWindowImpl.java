package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomWindowsImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMApplicationOnTaskPaneImpl;
import ru.kolaer.client.javafx.plugins.IApplication;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowImpl implements PCustomWindow {
	private static final Logger LOG = LoggerFactory.getLogger(PCustomWindowImpl.class);
	
	private final VCustomWindow view = new VCustomWindowsImpl();
	private IApplication application;
	private Pane parent;
	
	public PCustomWindowImpl() {
		this(null);
	}
	
	public PCustomWindowImpl(IApplication app) {
		this(null, app, app.getName());
	}

	public PCustomWindowImpl(Pane parent,IApplication app, String name) {
		this.parent = parent;
		this.application = app;
		this.view.setTitle(Optional.ofNullable(name).orElse(""));
		this.view.addLeftWindowIcon(new PCustomWindowIconImage(this));
		this.view.addRightWindowIcon(new PCustomWindowIconMinimize(this));
		this.view.addRightWindowIcon(new PCustomWindowIconMaximize(this));
		this.view.addRightWindowIcon(new PCustomWindowIconClose(this));	
	}
	
	@Override
	public VMApplicationOnTaskPane show() {
		
		final VMApplicationOnTaskPane taskPaneApp = new VMApplicationOnTaskPaneImpl(this);
		
		if(this.parent == null) {
			LOG.error("Parent == null!");
			return taskPaneApp;
		}
		
		if(this.application != null) {
			this.application.run();
			this.view.setContent(this.application.getContent());
		}
		this.view.setVisible(true);
		this.parent.getChildren().add(this.view.getWindow());
		
		return taskPaneApp;
	}

	@Override
	public void close() {
		this.view.setVisible(false);
		this.application.stop();
	}

	@Override
	public VCustomWindow getView() {
		return this.view;
	}

	@Override
	public Pane getParent() {
		return this.parent;
	}

	@Override
	public void setParent(Pane parent) {
		this.parent = parent;
	}

	@Override
	public void maximize() {
		this.view.setMaximize(!this.view.isMaximize());
	}

	@Override
	public void minimize() {
		this.view.setMinimize(!this.view.isMinimize());
	}

	@Override
	public IApplication getApplicationModel() {
		return this.application;
	}

	@Override
	public void setApplicationModel(IApplication application) {
		this.application = application;
	}
}
