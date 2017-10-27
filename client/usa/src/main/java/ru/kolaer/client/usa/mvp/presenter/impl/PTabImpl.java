package ru.kolaer.client.usa.mvp.presenter.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.mvp.presenter.PTab;
import ru.kolaer.client.usa.mvp.view.VTab;
import ru.kolaer.client.usa.mvp.viewmodel.impl.VTabImpl;
import ru.kolaer.client.usa.plugins.PluginBundle;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Реализация интерфейса {@linkplain PTab}. Содержит classLoader плагина.
 * @author Danilov
 * @version 0.1
 */
@Slf4j
public class PTabImpl implements PTab {
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
	public PTabImpl(PluginBundle plugin) {
		this.plugin = plugin;
		this.view = new VTabImpl();
		view.initView(initViewTab -> {
			view.setTitle(plugin.getNamePlugin());
			view.setContent(plugin.getUniformSystemPlugin().getContent());
		});
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
			ExecutorService threadRunPlugin = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Запуск плагина: " + this.plugin.getSymbolicNamePlugin());
				log.info("Запуск плагина: {}", plugin.getSymbolicNamePlugin());
					Tools.runOnWithOutThreadFX(() -> {
						try{
							plugin.getUniformSystemPlugin().initView(initUsp -> {
								view.setContent(initUsp.getContent());
								try {
									plugin.getUniformSystemPlugin().start();
								} catch (Exception e) {
									log.error("Ошибка при запуске плагина \"{}\"!", this.plugin.getSymbolicNamePlugin(), e);
									UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showErrorNotify(this.plugin.getNamePlugin(), e.getLocalizedMessage());
									this.closeTab();
								}
							});
						} catch(Exception ex) {
							log.error("Ошибка при интциализации UI плагина \"{}\"!", this.plugin.getSymbolicNamePlugin(), ex);
							UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().
									showErrorNotify(this.plugin.getNamePlugin(), ex.getLocalizedMessage());
						}
					});

					this.isActive = true;
				threadRunPlugin.shutdown();
			}, threadRunPlugin).exceptionally(t -> {
				log.error("Ошибка в работе плагина: {}", this.plugin.getSymbolicNamePlugin(), t);
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
		if(isActive) {
			ExecutorService threadStopPlugin = Executors.newSingleThreadExecutor();
			CompletableFuture.runAsync(() -> {
				Thread.currentThread().setName("Остановка плагина: " + plugin.getSymbolicNamePlugin());
				log.info("Остановка плагина: {}", plugin.getSymbolicNamePlugin());
				try {
					plugin.getUniformSystemPlugin().stop();
				} catch (Exception e) {
					log.error("Ошибка при остановке плагина \"{}\"!", plugin.getSymbolicNamePlugin(), e);

					UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
							.showErrorNotify(plugin.getNamePlugin(), e.getLocalizedMessage());
				}

				isActive = false;

				threadStopPlugin.shutdown();
			}, threadStopPlugin).exceptionally(t -> {
				log.error("Ошибка при остановке плагина: {}", plugin.getSymbolicNamePlugin(), t);
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
