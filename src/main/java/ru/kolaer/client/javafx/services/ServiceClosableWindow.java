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

import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.tools.Resources;

public class ServiceClosableWindow implements Service, ExplorerObserver {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceClosableWindow.class);
	private final RestTemplate restTemplate = new RestTemplate();	
	private final String username = System.getProperty("user.name");
	private final VMExplorer explorer;
	private boolean isRunning = false;
	private final List<PWindow> windows = new LinkedList<>();
	
	public ServiceClosableWindow(final VMExplorer explorer) {
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
			if(windows.size() > 0) {
				try {
					@SuppressWarnings("unchecked")
					Set<String> windowsClose = restTemplate.getForObject(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/windows/close").toString(), Set.class);
					windowsClose.forEach(windowName -> {
						Iterator<PWindow> iter = windows.iterator();
						while(iter.hasNext()) {
							final PWindow window = iter.next();
							if(windowName.equals(window.getApplicationModel().getName())) {
								window.close();
								iter.remove();
							}
						}	
					});
					windowsClose.clear();
				} catch(RestClientException ex) {
					LOG.error("Сервер \"{}\" не доступен!", new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/windows/close").toString());
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
	public void updateOpenWindow(final PWindow window) {
		this.windows.add(window);
		
		if(!this.isRunning)
			return;
		
		final ExecutorService singleThread = Executors.newSingleThreadExecutor();
		
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - открытие окна");
			this.restTemplate.postForLocation(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).append("/open").toString(), null);
		}, singleThread).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!",new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).append("/open").toString());
			return null;
		});
		
		singleThread.shutdown();
	}

	@Override
	public void updateCloseWindow(final PWindow window) {
		this.windows.remove(window);
		
		if(!this.isRunning)
			return;
		
		final ExecutorService singleThread = Executors.newSingleThreadExecutor();
		
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - закрытие окна");	
			this.restTemplate.postForLocation(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).append("/close").toString(), null);
		}, singleThread).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).append("/close").toString());
			return null;
		});
		
		singleThread.shutdown();
	}

	@Override
	public String getName() {
		return "Слушатель проводника";
	}
}