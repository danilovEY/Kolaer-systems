package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.mvp.view.impl.VTabImpl;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public class PTabImpl implements PTab {
	private final Logger LOG = LoggerFactory.getLogger(PTabImpl.class);
	
	private final IKolaerPlugin plugin;
	private VTab view;
	private boolean isActive = false;
	private final URLClassLoader loader;
	public PTabImpl(final URLClassLoader loader, final IKolaerPlugin plugin) {
		this.plugin = plugin;
		this.loader = loader;
		this.view = new VTabImpl(loader, plugin.getApplication());
	}

	@Override
	public VTab getView() {
		return this.view;
	}

	@Override
	public void setView(final VTab tab) {
		//this.view = tab;
	}

	@Override
	public IKolaerPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public void activeTab() {
		if(!this.isActive) {
			Thread.currentThread().setContextClassLoader(this.loader);
			CompletableFuture.supplyAsync(() -> {
				IApplication app = this.plugin.getApplication();
				app.run();
				return app;
			}).thenApply((app) -> {
				this.view.setContent(app.getContent());
				return app;
			}).exceptionally(t -> {
				t.printStackTrace(); 
				return null;	
			});
		}
	}

	@Override
	public void desActiveTab() {
		// TODO Auto-generated method stub
		
	}
	
	
}
