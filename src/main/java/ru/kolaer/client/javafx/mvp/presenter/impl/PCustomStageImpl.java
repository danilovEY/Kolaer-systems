package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;

public class PCustomStageImpl implements PCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(PCustomStageImpl.class);
	private final URLClassLoader classLoader;
	private final UniformSystemApplication application;
	
	private VCustomStage view;
	private VMApplicationOnTaskPane taskPane;
	
	public PCustomStageImpl(final UniformSystemApplication app) {
		this(app, Optional.ofNullable(app.getName()).orElse("Приложение"));
	}
	public PCustomStageImpl(final UniformSystemApplication app, final String name) {
		this((URLClassLoader) PCustomStageImpl.class.getClassLoader(), app, name);
	}
	
	public PCustomStageImpl(final URLClassLoader classLoader, final UniformSystemApplication app, final String name) {
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
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setName("Запуск плагина");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
			this.application.run();
			return this.application;
		}).exceptionally(t -> {
			LOG.error("Ошибка при запуске плагина!", t);
			return null;
		}).thenAccept((app) -> {
			this.view.setContent(app.getContent());
			this.view.centerOnScreen();
			this.view.setVisible(true);
		}).exceptionally(t -> {
			LOG.error("Ошибка при добавлении контента в окно плагина!", t);
			return null;
		});	
	}

	@Override
	public void close() {
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName("Завершение плагина");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
			this.view.setVisible(false);
			this.application.stop();
			LOG.info("Приложение \"{}\" остановлено!", this.application.getName());
		}).exceptionally(t -> {
			LOG.info("Ошибка при закрытии плагина", t);
			return null;
		}).thenRunAsync(() -> {
			this.taskPane.close();
		});
	}

	@Override
	public VCustomStage getView() {
		return this.view;
	}

	@Override
	public UniformSystemApplication getApplicationModel() {
		return this.application;
	}

	@Override
	public VMApplicationOnTaskPane getTaskPane() {
		return this.taskPane;
	}
	@Override
	public void setView(final VWindow view) {
		Platform.runLater(() -> {
			Thread.currentThread().setName("Конвертация окна");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			final VCustomStage stage = new VCustomStageImpl();
			stage.setContent(view.getContent());
			stage.setTitle(view.getTitle());
			stage.setVisible(view.isShowing());
			stage.setIconWindow(this.application.getIcon());
			stage.setOnCloseAction(e -> {
				this.close();
			});
			
			this.setView(stage);
		});
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
