package ru.kolaer.client.javafx.mvp.presenter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.mvp.view.impl.VTabImpl;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;

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
		this.view = new VTabImpl(plugin.getUniformSystemPlugin());
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
				} catch (final Exception e) {
					LOG.error("Ошибка при запуске плагина \"{}\"!", this.plugin.getSymbolicNamePlugin(), e);
					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getDialog().createErrorDialog(this.plugin.getNamePlugin(), "Ошибка при запуске плагина!").show();
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
				Thread.currentThread().setName("Остановка плагина: " + this.plugin.getSymbolicNamePlugin());
				try {
					this.plugin.getUniformSystemPlugin().stop();
				} catch (final Exception e) {
					LOG.error("Ошибка при остановке плагина \"{}\"!",this.plugin.getSymbolicNamePlugin(),e);
					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getDialog().createErrorDialog(this.plugin.getNamePlugin(), "Ошибка при остановке плагина!").show();
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
			Thread.currentThread().setName("Закрытие плагина: " + this.plugin.getSymbolicNamePlugin());
			this.deActiveTab();
			this.view.closeTab();

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
