package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMApplicationOnTaskPaneImpl;
import ru.kolaer.client.javafx.plugins.IApplication;

public class PCustomStageImpl implements PCustomStage{
	private static final Logger LOG = LoggerFactory.getLogger(PCustomStageImpl.class);	
	
	private final VCustomStage view = new VCustomStageImpl();
	private final IApplication<Object> application;
	private final VMApplicationOnTaskPane taskPane;
	private final ExecutorService appThread = Executors.newSingleThreadExecutor();
	
	public PCustomStageImpl() {
		this(null);
	}
	
	public PCustomStageImpl(IApplication<Object> app) {
		this(app, app.getName());
	}

	public PCustomStageImpl(IApplication<Object> app, String name) {
		this.application = app;
		LOG.debug("app.name: {}, app.icon: {}", app.getName(), app.getIcon());
		this.view.setTitle(Optional.ofNullable(name).orElse(""));
		this.view.setIconWindow(app.getIcon());
		this.view.setOnCloseAction(e -> {
			this.close();
		});
		this.taskPane = new VMApplicationOnTaskPaneImpl(this);
	}
	
	@Override
	public void show() {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			Thread.currentThread().setName("Запуск приложения");
			try {
				appThread.submit(this.application);
			} catch (Exception e) {
				e.printStackTrace();
			}
			appThread.shutdown();
			LOG.info("Приложение \"{}\" запущено!", this.application.getName());
			this.view.setContent(this.application.getContent());
			this.taskPane.show();
			this.view.setVisible(true);
		});
		thread.shutdown();
	}

	@Override
	public void close() {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			Thread.currentThread().setName("Завершение приложения");
			this.view.setVisible(false);
			this.application.stop();
			appThread.shutdownNow();
			
			this.view.setContent(null);
			this.taskPane.close();
		});
		thread.shutdown();
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

	@Override
	public VMApplicationOnTaskPane getTaskPane() {
		return this.taskPane;
	}
}
