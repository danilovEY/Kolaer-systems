package ru.kolaer.client.javafx.services;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObservable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.system.JsonConverterSinleton;
import ru.kolaer.client.javafx.tools.Resources;

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
	private final WebResource webResource;
	/**Имя пользователя.*/
	private final String username = System.getProperty("user.name");
	private final ExplorerObservable explorer;

	private boolean isRunning = false;
	/**Список активных плагинов.*/
	private final List<RemoteActivationDeactivationPlugin> plugins = new LinkedList<>();
	
	public ServiceRemoteActivOrDeactivPlugin(final ExplorerObservable explorer, final WebResource webResource) {
		this.explorer = explorer;

		this.webResource = webResource;
		this.explorer.registerObserver(this);
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		
		//this.updateAddPlugin(new MainRemoteActivDeactivPlugin());
		
		Thread.currentThread().setName("Прослушивание внутреннего эксплорера");
		while(this.isRunning) {
			if(plugins.size() >= 0) {
				try {
					LOG.info("Получение....." );
					final List<String> pluginsClose = JsonConverterSinleton.getInstance().getEntitys(webResource.path("user").path(username).path("app").path("close"),String.class);
					for(final String tabName : pluginsClose) {
						LOG.info("Закрыть плагин: {}", tabName );
						/*final Iterator<RemoteActivationDeactivationPlugin> iter = plugins.iterator();
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
						}	*/
					}
				} catch(final UniformInterfaceException | ClientHandlerException ex) {
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
			webResource.path("user").path(username).path("app").path(plugin.getName()).path("open").header("Content-Type","application/json; charset=UTF-8").post();
			threadPush.shutdown();
		}, threadPush).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!",new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/open").toString(), t);
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
			webResource.path("user").path(username).path("app").path(plugin.getName()).path("close").header("Content-Type","application/json; charset=UTF-8").post();
			threadPush.shutdown();
		}, threadPush).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/close").toString(), t);
			return null;
		});
	}

	@Override
	public void updateAddPlugin(final RemoteActivationDeactivationPlugin tab) {
		this.plugins.add(tab);
	}
}