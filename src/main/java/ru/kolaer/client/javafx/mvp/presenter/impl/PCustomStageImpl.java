package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.IApplication;

public class PCustomStageImpl implements PCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(PCustomStageImpl.class);
	private final URLClassLoader classLoader;
	private final IApplication application;
	
	private VCustomStage view;
	private VMApplicationOnTaskPane taskPane;
	
	public PCustomStageImpl(final IApplication app) {
		this(app, Optional.ofNullable(app.getName()).orElse("Приложение"));
	}
	public PCustomStageImpl(final IApplication app, final String name) {
		this((URLClassLoader) PCustomStageImpl.class.getClassLoader(), app, name);
	}
	
	public PCustomStageImpl(final URLClassLoader classLoader, final IApplication app, final String name) {
		this.application = app;
		this.classLoader = classLoader;
		if(this.application == null){
			final NullPointerException ex = new NullPointerException("Application == null!");
			LOG.error("Application == null!", ex);
			throw ex;
		}
	}

	@Override
	public void show() {
		System.out.println("000");
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setName("Запуск приложения");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			System.out.println("AAA");
			this.application.run();
			return this.application;
		}).thenAccept((app) -> {
			System.out.println("BBB");
			this.view.setContent(app.getContent());
			this.view.centerOnScreen();
			this.view.setVisible(true);
		}).thenRunAsync(() -> {
			this.taskPane.show();
			LOG.info("Приложение \"{}\" запущено!", this.application.getName());
		});		
	}

	@Override
	public void close() {
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			Thread.currentThread().setName("Завершение приложения");
			this.view.setVisible(false);		
			this.taskPane.close();
			this.application.stop();
			try {
				this.classLoader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	@Override
	public void setView(final VWindow view) {
		final VCustomStage stage = new VCustomStageImpl();
		stage.setContent(view.getContent());
		stage.setTitle(view.getTitle());
		stage.setVisible(view.isShowing());
		stage.setIconWindow(this.application.getIcon());
		stage.setOnCloseAction(e -> {
			this.close();
		});
		
		this.setView(stage);
	}
	
	@Override
	public void setTaskPane(final VMApplicationOnTaskPane taskPane) {
		this.taskPane = taskPane;
	}
	@Override
	public void setView(final VCustomStage view) {
		this.view = view;
	}
}
