package ru.kolaer.client.javafx.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.mvp.view.impl.VTabImpl;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

import java.net.URLClassLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Реализация интерфейса {@linkplain PTab}. Содержит classLoader плагина.
 * @author Danilov
 * @version 0.1
 */
public class PTabImpl implements PTab {
	private final Logger LOG = LoggerFactory.getLogger(PTabImpl.class);
	/**Плагин.*/
	private final UniformSystemPlugin plugin;
	/**Приложение плагина.*/
	private final UniformSystemApplication app;
	/**View.*/
	private final VTab view;
	/**Флаг активации вкладки.*/
	private boolean isActive = false;
	/**ClassLoader плагина.*/
	private final URLClassLoader loader;
	private final UniformSystemEditorKit editorKid;
	/**
	 * {@linkplain PTabImpl}
	 * @param loader - Загрузчик плагина.
	 * @param plugin - Плагин.
	 */
	public PTabImpl(final URLClassLoader loader, final UniformSystemPlugin plugin, final UniformSystemEditorKit editorKid) {
		this.plugin = plugin;
		this.loader = loader;
		this.app = this.plugin.getApplication();
		this.editorKid = editorKid;
		this.view = new VTabImpl(loader, this.app);
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
	public UniformSystemPlugin getModel() {
		return this.plugin;
	}

	@Override
	public void activeTab() {
		if(!this.isActive) {
			final ExecutorService threadRunPlugin = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Запуск плагина: " + this.plugin.getName());
				Thread.currentThread().setContextClassLoader(this.loader);
				try {
					this.app.start();
					
					this.view.setContent(app.getContent());
				} catch (final Exception e) {
					LOG.error("Ошибка при запуске плагина \"{}\"!", this.plugin.getName(), e);
					this.editorKid.getUISystemUS().getDialog().showErrorDialog(this.plugin.getName(), "Ошибка при запуске плагина!");
				}
				threadRunPlugin.shutdown();
			}, threadRunPlugin);

			this.isActive = true;	
		}
	}

	@Override
	public void deActiveTab() {
		if(this.isActive) {
			final ExecutorService threadStopPlugin = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Остановка плагина: " + this.plugin.getName());
				Thread.currentThread().setContextClassLoader(this.loader);
				try {
					this.app.stop();
				} catch (final Exception e) {
					LOG.error("Ошибка при остановке плагина \"{}\"!",this.plugin.getName(),e);
					this.editorKid.getUISystemUS().getDialog().showErrorDialog(this.plugin.getName(), "Ошибка при остановке плагина!");
				}
				threadStopPlugin.shutdown();
			}, threadStopPlugin);

			this.view.setContent(null);
			this.isActive = false;
		}
	}

	@Override
	public void closeTab() {
		final ExecutorService threadClosePlugin = Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName("Закрытие плагина: " + this.plugin.getName());
			this.deActiveTab();
			this.view.closeTab();
			try {
				this.loader.clearAssertionStatus();
				this.loader.close();
			} catch (Exception e) {
				LOG.error("Ошибка при закрытии clssloader'а.", e);
				throw new RuntimeException(e);
			}
			threadClosePlugin.shutdown();
		}, threadClosePlugin).exceptionally(t -> {
			LOG.error("Ошибка при закрытии приложения: {}", this.app.getName(), t);
			threadClosePlugin.shutdownNow();
			System.exit(-9);
			return null;
		});
	}

	@Override
	public void activation() {
		//TODO Сделать загрузку плагина.
	}

	@Override
	public void deactivation() {
		this.closeTab();
	}

	@Override
	public String getName() {
		return this.plugin.getName();
	}
}
