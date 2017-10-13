package ru.kolaer.client.usa.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.usa.mvp.presenter.PTab;
import ru.kolaer.client.usa.mvp.view.VTab;
import ru.kolaer.client.usa.mvp.view.impl.VTabImpl;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

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
	private final PluginBundle plugin;
	/**View.*/
	private final VTab view;
	/**Флаг активации вкладки.*/
	private boolean isActive = false;
	/**
	 * {@linkplain PTabImpl}
	 * @param plugin - Плагин.
	 */
	public PTabImpl(final PluginBundle plugin) {
		this.plugin = plugin;
		this.view = new VTabImpl();
		this.view.setTitle(plugin.getNamePlugin());
		this.view.setContent(plugin.getUniformSystemPlugin().getContent());
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
	public PluginBundle getModel() {
		return this.plugin;
	}

	@Override
	public void activeTab() {
		if(!this.isActive) {
			final ExecutorService threadRunPlugin = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Запуск плагина: " + this.plugin.getSymbolicNamePlugin());
				try {
					this.plugin.getUniformSystemPlugin().start();
					
					this.view.setContent(plugin.getUniformSystemPlugin().getContent());

					this.isActive = true;
				} catch (final Exception e) {
					LOG.error("Ошибка при запуске плагина \"{}\"!", this.plugin.getSymbolicNamePlugin(), e);
					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showErrorNotifi(this.plugin.getNamePlugin(), "Ошибка при запуске плагина!");
					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showErrorNotifi(this.plugin.getNamePlugin(), e.getMessage());
					this.closeTab();
				}
				threadRunPlugin.shutdown();
			}, threadRunPlugin).exceptionally(t -> {
				LOG.error("Ошибка в работе плагина: {}", this.plugin.getSymbolicNamePlugin(), t);
				threadRunPlugin.shutdownNow();
				return null;
			});
		}
	}

	@Override
	public void deActiveTab() {

	}

	@Override
	public void closeTab() {
		if(this.isActive) {
			final ExecutorService threadStopPlugin = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Остановка плагина: " + this.plugin.getSymbolicNamePlugin());
				try {
					this.plugin.getUniformSystemPlugin().stop();
				} catch (final Exception e) {
					LOG.error("Ошибка при остановке плагина \"{}\"!",this.plugin.getSymbolicNamePlugin(), e);
					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showErrorNotifi(this.plugin.getNamePlugin(), "Ошибка при остановке плагина!");
					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showErrorNotifi(this.plugin.getNamePlugin(), e.getMessage());
				}

				this.isActive = false;

				threadStopPlugin.shutdown();
			}, threadStopPlugin).exceptionally(t -> {
				LOG.error("Ошибка при остановке плагина: {}", this.plugin.getSymbolicNamePlugin(), t);
				threadStopPlugin.shutdownNow();
				return null;
			});
		}
		this.view.closeTab();
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
		return this.plugin.getNamePlugin();
	}
}
