package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;

/**
 * Имплеминтация {@linkplain PCustomStage}. При открытии окна, 
 * создает view-панель для панели задач.
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomStageImpl implements PCustomStage {
	private final Logger LOG = LoggerFactory.getLogger(PCustomStageImpl.class);
	
	/**Класс-лоадер плагина.*/
	private final URLClassLoader classLoader;
	/**Приложение плагина.*/
	private final UniformSystemApplication application;
	/**View окна.*/
	private VCustomStage view;
	/**View плагина на панели задач.*/
	private VMApplicationOnTaskPane taskPane;
	
	/**
	 * {@linkplain PCustomStageImpl}.
	 * @param app - Плагин.
	 */
	public PCustomStageImpl(final UniformSystemApplication app) {
		this(app, Optional.ofNullable(app.getName()).orElse("Приложение"));
	}
	
	/**
	 * {@linkplain PCustomStageImpl}.
	 * @param app - Плагин.
	 * @param name - Имя плагина.
	 */
	public PCustomStageImpl(final UniformSystemApplication app, final String name) {
		this((URLClassLoader) PCustomStageImpl.class.getClassLoader(), app, name);
	}
	
	/**
	 * {@linkplain PCustomStageImpl}.
	 * @param app - Плагин.
	 * @param name - Имя плагина.
	 * @param classLoader - класс-лоадер плагина.
	 * 
	 * @throws NullPointerException - если плагин null.
	 */
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
			
			try {
				this.application.run();
			} catch (final Exception e) {
				LOG.error("Ошибка при запуске плагина!", e);
			}
			return this.application;
		}).exceptionally(t -> {
			LOG.error("Ошибка при запуске плагина!", t);
			this.close();
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
			try {
				this.application.stop();
			} catch (Exception e) {
				LOG.info("Ошибка при закрытии плагина", e);
			}
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
		final VCustomStage stage = new VCustomStageImpl(this.classLoader);
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
