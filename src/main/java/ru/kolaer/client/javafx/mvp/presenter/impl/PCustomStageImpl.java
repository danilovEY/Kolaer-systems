package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMApplicationOnTaskPaneImpl;
import ru.kolaer.client.javafx.plugins.IApplication;

public class PCustomStageImpl implements PCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(PCustomStageImpl.class);

	private final VCustomStage view = new VCustomStageImpl();
	private final IApplication application;
	private final VMApplicationOnTaskPane taskPane;

	public PCustomStageImpl(final IApplication app) {
		this(app, app.getName());
	}

	public PCustomStageImpl(final IApplication app, final String name) {
		this.application = app;
		if(this.application == null){
			LOG.error("Application == null!");
			throw new RuntimeException("Application == null!");
		}
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
			CompletableFuture.runAsync(this.application).thenAccept((e) -> {
				this.view.setContent(this.application.getContent());
				this.view.centerOnScreen();
			});
			this.taskPane.show();
			this.view.setVisible(true);
			LOG.info("Приложение \"{}\" запущено!", this.application.getName());
		});
		thread.shutdown();
	}

	@Override
	public void close() {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			Thread.currentThread().setName("Завершение приложения");
			this.view.setVisible(false);		
			this.taskPane.close();
			this.application.stop();
			LOG.info("Приложение \"{}\" остановлено!", this.application.getName());
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
	public VMApplicationOnTaskPane getTaskPane() {
		return this.taskPane;
	}
}
