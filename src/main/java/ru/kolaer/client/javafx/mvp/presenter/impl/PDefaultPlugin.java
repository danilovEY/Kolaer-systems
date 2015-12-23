package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PPlugin;
import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMLabelImpl;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class PDefaultPlugin implements PPlugin{
	private static final Logger LOG = LoggerFactory.getLogger(PDefaultPlugin.class);
	
	private final IKolaerPlugin plugin;
	private final VMLabel label;
	private final Pane taskPane;
	private final URLClassLoader classLoader;
	private PWindow window;
	
	public PDefaultPlugin(final IKolaerPlugin plugin) {
		this(plugin, null);
	}
	
	public PDefaultPlugin(final IKolaerPlugin plugin, final Pane taskPane) {
		this((URLClassLoader)PDefaultPlugin.class.getClassLoader(), plugin, taskPane);
	}
	
	public PDefaultPlugin(final URLClassLoader loader, final IKolaerPlugin plugin, final Pane taskPane) {
		this.plugin = plugin;
		this.taskPane = taskPane;
		this.classLoader = loader;
		this.label = new VMLabelImpl(this.classLoader, plugin.getLabel());
		this.init();
	}
	
	private void init() {
		this.label.setOnAction(e -> {
			if(this.window == null) {
				CompletableFuture.runAsync(() -> {
					Thread.currentThread().setName("Инициализация окна плагина: " + this.plugin.getName());		
					this.window = new PCustomStageImpl(this.classLoader, this.plugin.getApplication(), this.plugin.getApplication().getName());
				}).thenRun(() -> {
					Thread.currentThread().setName("Запуск окна плагина: " + this.plugin.getName());				
					this.window.getTaskPane().show();
					this.window.show();				
				});				
			} else {
				this.window.getTaskPane().show();
				this.window.show();	
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
	}}
