package ru.kolaer.client.javafx.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.presenter.impl.PMainApplication;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerTabsObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerTabsObserver;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.tools.Resources;

public class ServiceClosableTab implements Service, ExplorerTabsObserver {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceClosableTab.class);
	
	private final RestTemplate restTemplate = new RestTemplate();	
	private final String username = System.getProperty("user.name");
	private final ExplorerTabsObresvable explorer;
	private boolean isRunning = false;
	private final List<PTab> tabs = new LinkedList<>();
	
	public ServiceClosableTab(final ExplorerTabsObresvable explorer) {
		this.explorer = explorer;
		this.explorer.registerObserver(this);
	}
	
	@Override
	public void run() {
		this.isRunning = true;
		
		final PTab mainTab = new PTab() {
			final IApplication app = new PMainApplication();
			@Override
			public void setView(VTab tab) {

			}
			
			@Override
			public VTab getView() {
				return null;
			}
			
			@Override
			public IKolaerPlugin getPlugin() {
				return null;
			}
			
			@Override
			public IApplication getModel() {
				return app;
			}
			
			@Override
			public void deActiveTab() {

			}
			
			@Override
			public void closeTab() {
				app.stop();
			}
			
			@Override
			public void activeTab() {
				
			}
		};
		this.updateOpenTab(mainTab);
		
		Thread.currentThread().setName("Прослушивание внутреннего эксплорера");
		while(this.isRunning) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(tabs.size() > 0) {
				try {
					@SuppressWarnings("unchecked")
					Set<String> tabsClose = restTemplate.getForObject(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/app/close").toString(), Set.class);
					tabsClose.forEach(tabName -> {
						Iterator<PTab> iter = tabs.iterator();
						while(iter.hasNext()) {
							final PTab tab = iter.next();
							if(tabName.equals(tab.getModel().getName())) {
								tab.closeTab();
								iter.remove();
							}
						}	
					});
					tabsClose.clear();
				} catch(RestClientException ex) {
					LOG.error("Сервер \"{}\" не доступен!", new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/app/close").toString());
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
	public void updateOpenTab(PTab tab) {
		this.tabs.add(tab);
		
		if(!this.isRunning)
			return;
		
		final ExecutorService singleThread = Executors.newSingleThreadExecutor();
		
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - открытие окна");
			this.restTemplate.postForLocation(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/app/").append(tab.getModel().getName()).append("/open").toString(), null);
		}, singleThread).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!",new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/app/").append(tab.getModel().getName()).append("/open").toString());
			return null;
		});
		
		singleThread.shutdown();
	}

	@Override
	public void updateCloseTab(PTab tab) {
		this.tabs.remove(tab);
		
		if(!this.isRunning)
			return;
		
		final ExecutorService singleThread = Executors.newSingleThreadExecutor();
		
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - закрытие окна");	
			this.restTemplate.postForLocation(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/app/").append(tab.getModel().getName()).append("/close").toString(), null);
		}, singleThread).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/app/").append(tab.getModel().getName()).append("/close").toString());
			return null;
		});
		
		singleThread.shutdown();
	}
}