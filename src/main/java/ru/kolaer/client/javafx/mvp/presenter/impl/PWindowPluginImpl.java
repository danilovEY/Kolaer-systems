package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PCustomStage;
import ru.kolaer.client.javafx.mvp.presenter.PPlugin;
import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.presenter.PWindowPlugin;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;
import ru.kolaer.client.javafx.mvp.view.impl.VCustomStageImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerWindowsObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMApplicationOnTaskPaneImpl;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMLabelImpl;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class PWindowPluginImpl implements PWindowPlugin {
	private static final Logger LOG = LoggerFactory.getLogger(PWindowPluginImpl.class);
	private ExplorerWindowsObserver explorer;
	private final IKolaerPlugin plugin;
	private final VMLabel label;
	private final Pane taskPane;
	private final URLClassLoader classLoader;
	private PCustomStage window;
	
	public PWindowPluginImpl(final IKolaerPlugin plugin) {
		this(plugin, null);
	}
	
	public PWindowPluginImpl(final IKolaerPlugin plugin, final Pane taskPane) {
		this((URLClassLoader)PWindowPluginImpl.class.getClassLoader(), plugin, taskPane);
	}
	
	public PWindowPluginImpl(final URLClassLoader loader, final IKolaerPlugin plugin, final Pane taskPane) {
		this.plugin = plugin;
		this.taskPane = taskPane;
		this.classLoader = loader;
		this.label = new VMLabelImpl(this.classLoader, plugin.getLabel());
		this.init();
	}
	
	private void init() {
		this.label.setOnAction(e -> {
			if(this.window == null) {
				CompletableFuture.supplyAsync(() -> {
					Thread.currentThread().setName("Инициализация окна плагина: " + this.plugin.getName());		
					this.window = new PCustomStageImpl(this.classLoader, this.plugin.getApplication(), this.plugin.getApplication().getName());
					return this.window;
				}).exceptionally((t) -> {
					LOG.error("Ошибка при инициализации окна плагина!", t);
					return null;
				}).thenApplyAsync(window -> {
					Platform.runLater(() -> {
						Thread.currentThread().setName("Инициализация view окна плагина: " + this.plugin.getName());
						Thread.currentThread().setContextClassLoader(this.classLoader);
						final VCustomStage stage = new VCustomStageImpl(this.classLoader);
						stage.setTitle(this.plugin.getApplication().getName());
						stage.setIconWindow(this.plugin.getApplication().getIcon());
						stage.setOnCloseAction(event -> {
							CompletableFuture.runAsync(() -> {
								this.window.close();
							}).thenRunAsync(() -> {
								this.notifyCloseWindow(this.window);
							});
						});
						window.setView(stage);
					});
					return window;			
				}).exceptionally((t) -> {
					LOG.error("Ошибка при инициализация view окна!", t);
					return null;
				}).thenAccept((window) -> {
					Thread.currentThread().setName("Инициализация формы для панели задач плагина: " + this.plugin.getName());
					final VMApplicationOnTaskPane taskPane = new VMApplicationOnTaskPaneImpl(this.classLoader, window, this.taskPane);
					window.setTaskPane(taskPane);
				}).exceptionally((t) -> {
					LOG.error("Ошибка при инициализация формы!", t);
					return null;
				}).thenRunAsync(() -> {
					Thread.currentThread().setName("Запуск окна плагина: " + this.plugin.getName());				
					this.window.show();					
				}).exceptionally((t) -> {
					LOG.error("Ошибка открытии окна плагина!", t);
					return null;
				}).thenRunAsync(() -> {
					this.window.getTaskPane().show();
				}).exceptionally(t -> {
					LOG.error("Ошибка при запуске формы для панели задач!", t);
					return null;
				}).thenRunAsync(() -> {
					this.notifyOpenWindow(this.window);
				});
			} else {
				CompletableFuture.runAsync(() -> {
					Thread.currentThread().setName("Запуск окна плагина: " + this.plugin.getName());				
					this.window.show();					
				}).exceptionally((t) -> {
					LOG.error("Ошибка открытии окна плагина!", t);
					return null;
				}).thenRunAsync(() -> {
					this.notifyOpenWindow(this.window);
				});
			}			
		});
	}
	
	@Override
	public VMLabel getVMLabel() {
		return this.label;
	}

	@Override
	public PWindow getWindow() {
		return this.window;
	}

	@Override
	public IKolaerPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public void notifyOpenWindow(final PWindow window) {
		if(this.explorer != null)
			this.explorer.updateOpenWindow(window);
	}

	@Override
	public void notifyCloseWindow(PWindow window) {
		if(this.explorer != null)
			this.explorer.updateCloseWindow(window);
	}

	@Override
	public void registerObserver(ExplorerWindowsObserver observer) {
		this.explorer = observer;
	}

	@Override
	public void removeObserver(ExplorerWindowsObserver observer) {
		this.explorer = null;
	}}
