package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.mvp.view.impl.VTabImpl;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class PTabImpl implements PTab {
	private final Logger LOG = LoggerFactory.getLogger(PTabImpl.class);
	
	private final IKolaerPlugin plugin;
	private final IApplication app;
	private final VTab view;
	private boolean isActive = false;
	private final URLClassLoader loader;
	public PTabImpl(final URLClassLoader loader, final IKolaerPlugin plugin) {
		this.plugin = plugin;
		this.loader = loader;
		this.app = this.plugin.getApplication();
		this.view = new VTabImpl(loader, plugin.getApplication());
	}

	@Override
	public VTab getView() {
		return this.view;
	}

	@Override
	public void setView(final VTab tab) {
		this.view.setContent(tab.getContent().getContent());
	}

	@Override
	public IKolaerPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public void activeTab() {
		if(!this.isActive) {
			final ExecutorService treadActTab = Executors.newSingleThreadExecutor();
			CompletableFuture.supplyAsync(() -> {
				Thread.currentThread().setName("Запуск плагина: " + this.plugin.getName());
				Thread.currentThread().setContextClassLoader(this.loader);
				this.app.run();
				return app;
			}, treadActTab).exceptionally(t -> {
				LOG.error("Ошибка при запуске плагина \"{}\"!",this.plugin.getName(),t);
				return null;	
			}).thenApplyAsync((app) -> {
				this.view.setContent(app.getContent());
				this.isActive = true;
				treadActTab.shutdown();
				return app;
			}).exceptionally(t -> {
				LOG.error("Ошибка при отображении плагина \"{}\"!",this.plugin.getName(),t);
				return null;	
			});
		}
	}

	@Override
	public void deActiveTab() {
		if(this.isActive) {
			final ExecutorService treadDesActTab = Executors.newSingleThreadExecutor();
			CompletableFuture.supplyAsync(() -> {
				Thread.currentThread().setName("Остановка плагина: " + this.plugin.getName());
				Thread.currentThread().setContextClassLoader(this.loader);
				this.app.stop();
				return app;
			}, treadDesActTab).exceptionally(t -> {
				LOG.error("Ошибка при запуске плагина \"{}\"!",this.plugin.getName(),t);
				return null;	
			}).thenApplyAsync((app) -> {
				this.view.setContent(null);
				this.isActive = false;
				treadDesActTab.shutdown();
				return app;
			}).exceptionally(t -> {
				LOG.error("Ошибка при отображении плагина \"{}\"!",this.plugin.getName(),t);
				return null;	
			});	
		}
	}
	
	
}
