package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.net.URLClassLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.layout.FlowPane;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.mvp.view.impl.VTabImpl;
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
			Platform.runLater(() -> {
				CompletableFuture.runAsync(this.plugin.getApplication()).exceptionally(t -> {
					LOG.error("Ошибка!", t);
					return null;
				}).thenRun(() -> {
					Platform.runLater(() -> {
						Thread.currentThread().setContextClassLoader(this.loader);
						try {
							TimeUnit.SECONDS.sleep(5);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.view.getTab().setContent(new FlowPane(this.plugin.getApplication().getContent()));
					});
				});
			});
		}
	}

	@Override
	public void desActiveTab() {
		// TODO Auto-generated method stub
		
	}
	
	
}
