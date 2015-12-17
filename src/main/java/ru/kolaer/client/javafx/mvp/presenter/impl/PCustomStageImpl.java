package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.plugins.IApplication;

public class PCustomStageImpl implements PCustomStage{
	private static final Logger LOG = LoggerFactory.getLogger(PCustomStageImpl.class);	
	
	private final VCustomStage view = new VCustomStageImpl();
	private final IApplication application;
	private boolean isShowing = false;
	
	public PCustomStageImpl() {
		this(null);
	}
	
	public PCustomStageImpl(IApplication app) {
		this(app, app.getName());
	}

	public PCustomStageImpl(IApplication app, String name) {
		this.application = app;
		LOG.debug("app.name: {}, app.icon: {}", app.getName(), app.getIcon());
		this.view.setTitle(Optional.ofNullable(name).orElse(""));
		this.view.setIconWindow(app.getIcon());
	}
	
	@Override
	public void show() {
		if(!this.isShowing){
			if(this.application != null) {
				this.application.run();
				LOG.info("Приложение \"{}\" запущено!", this.application.getName());
				this.view.setContent(this.application.getContent());
			}
		}
		this.view.setVisible(true);
	}

	@Override
	public void close() {
		this.view.setVisible(false);
		
		if(this.application != null)
			this.application.stop();	
		this.view.setContent(null);
	}

	@Override
	public VCustomStage getView() {
		return this.view;
	}

	@Override
	public IApplication getApplicationModel() {
		return this.application;
	}

	@Override
	public void setApplicationModel(IApplication application) {
		
	}
}
