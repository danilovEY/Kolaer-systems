package ru.kolaer.client.javafx.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.tools.Resources;

public class ServiceRemoteActivOrDeactivPlugin implements Service, ExplorerObserver {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceRemoteActivOrDeactivPlugin.class);
	
	private final RestTemplate restTemplate = new RestTemplate();	
	private final String username = System.getProperty("user.name");
	private final ExplorerObresvable explorer;
	private boolean isRunning = false;
	private final List<RemoteActivationDeactivationPlugin> plugins = new LinkedList<>();
	
	public ServiceRemoteActivOrDeactivPlugin(final ExplorerObresvable explorer) {
		this.explorer = explorer;
		this.explorer.registerObserver(this);
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		
		Thread.currentThread().setName("Прослушивание внутреннего эксплорера");
		while(this.isRunning) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(plugins.size() > 0) {
				try {
					@SuppressWarnings("unchecked")
					Set<String> pluginsClose = restTemplate.getForObject(new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/close").toString(), Set.class);
					pluginsClose.forEach(tabName -> {
						Iterator<RemoteActivationDeactivationPlugin> iter = plugins.iterator();
						while(iter.hasNext()) {
							final RemoteActivationDeactivationPlugin plugin = iter.next();
							if(tabName.equals(plugin.getName())) {
								plugin.deactivation();
								iter.remove();
							}
						}	
					});
					pluginsClose.clear();
				} catch(RestClientException ex) {
					LOG.error("Сервер \"{}\" не доступен!", new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/close").toString());
				}
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

		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - открытие окна");
			this.restTemplate.postForLocation(new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/open").toString(), null);
		}).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!",new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/open").toString());
			return null;
		});
	}

	@Override
	public void updateDeactivationPlugin(final RemoteActivationDeactivationPlugin plugin) {
		if(!this.isRunning)
			return;

		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - закрытие окна");	
			this.restTemplate.postForLocation(new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/close").toString(), null);
		}).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/").append(username).append("/app/").append(plugin.getName()).append("/close").toString());
			return null;
		});
	}

	@Override
	public void updateAddPlugin(final RemoteActivationDeactivationPlugin tab) {
		this.plugins.add(tab);
	}
}