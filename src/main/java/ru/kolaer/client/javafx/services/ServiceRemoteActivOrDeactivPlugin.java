package ru.kolaer.client.javafx.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.mvp.presenter.PDialog;
import ru.kolaer.api.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.plugins.main.MainRemoteActivDeactivPlugin;
import ru.kolaer.client.javafx.tools.Resources;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Служба для удаленного активации/дезактивации плагина.
 * 
 * @author danilovey
 * @version 0.1
 */
public class ServiceRemoteActivOrDeactivPlugin implements Service, ExplorerObserver {
	private final Logger LOG = LoggerFactory.getLogger(ServiceRemoteActivOrDeactivPlugin.class);
	/**Объект для взаимодействия с REST.*/
	private final RestTemplate restTemplate = new RestTemplate();	
	/**Имя пользователя.*/
	private final String username = System.getProperty("user.name");
	private final ExplorerObresvable explorer;
	private final UniformSystemEditorKit editorKit;
	private boolean isRunning = false;
	/**Список активных плагинов.*/
	private final List<RemoteActivationDeactivationPlugin> plugins = new LinkedList<>();
	
	public ServiceRemoteActivOrDeactivPlugin(final ExplorerObresvable explorer, final UniformSystemEditorKit editorKit) {
		this.explorer = explorer;
		this.editorKit = editorKit;
		this.explorer.registerObserver(this);
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		
		this.updateAddPlugin(new MainRemoteActivDeactivPlugin());
		
		Thread.currentThread().setName("Прослушивание внутреннего эксплорера");
		while(this.isRunning) {
			if(plugins.size() > 0) {
				try {
					final String[] pluginsClose = restTemplate.getForObject(new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/close").toString(), String[].class);
					for(final String tabName : pluginsClose) {
						LOG.debug("Закрыть плагин: {}", tabName );
						final Iterator<RemoteActivationDeactivationPlugin> iter = plugins.iterator();
						while(iter.hasNext()) {
							final RemoteActivationDeactivationPlugin plugin = iter.next();
							if(tabName.equals(plugin.getName())) {
								final ExecutorService threadPush= Executors.newSingleThreadExecutor();
								CompletableFuture.runAsync(() -> {
									final PDialog dialog = this.editorKit.getUISystemUS().getDialog().createInfoDialog("Внимание! Пришел запрос с сервера!", "Через 5 секунд закроется: \"" + tabName + "\"");
									dialog.show();
									try {
										TimeUnit.SECONDS.sleep(5);
									} catch (Exception e) {
										LOG.error("Ошибка!", e);
									}
									plugin.deactivation();
									dialog.close();
									threadPush.shutdown();
								}, threadPush).exceptionally(t -> {
									LOG.error("Ошибка при закрытии плагина!", t);
									plugin.deactivation();
									return null;
								});
								
								iter.remove();
							}
						}	
					}
				} catch(final RestClientException ex) {
					LOG.error("Сервер \"{}\" не доступен!", new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/close").toString());
				}
			}
			
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (Exception e) {
				LOG.error("Ошибка!", e);
				this.isRunning = false;
				return;
			}
		}
	}

	@Override
	public void setRunningStatus(boolean isRun) {
		this.isRunning = isRun;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
	public void stop() {
		this.isRunning = false;
		this.explorer.removeObserver(this);
	}

	@Override
	public String getName() {
		return "Слушатель проводника";
	}

	@Override
	public void updateActivationPlugin(final RemoteActivationDeactivationPlugin plugin) {
		if(!this.isRunning)
			return;
		final ExecutorService threadPush= Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - открытие окна");
			this.restTemplate.postForLocation(new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/open").toString(), null);
			threadPush.shutdown();
		}, threadPush).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!",new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/open").toString());
			return null;
		});
	}

	@Override
	public void updateDeactivationPlugin(final RemoteActivationDeactivationPlugin plugin) {
		if(!this.isRunning)
			return;
		final ExecutorService threadPush= Executors.newSingleThreadExecutor();
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - закрытие окна");	
			this.restTemplate.postForLocation(new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/close").toString(), null);
			threadPush.shutdown();
		}, threadPush).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/close").toString());
			return null;
		});
	}

	@Override
	public void updateAddPlugin(final RemoteActivationDeactivationPlugin tab) {
		this.plugins.add(tab);
	}
}