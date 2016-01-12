package ru.kolaer.client.javafx.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
	private final List<PWindow> windows = new ArrayList<>();
	
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
					ResponseEntity<Set<String>> window = restTemplate.exchange(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/windows/close").toString(), HttpMethod.GET, null, new ParameterizedTypeReference<Set<String>>(){} );
					//Set<String> set = new HashSet<>();
					//set.addAll(Arrays.asList(window));
					System.out.println("WIN: " + window.getBody().size());
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
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - открытие окна");
			
			if(!this.isRunning)
				return;
			
			this.restTemplate.postForLocation(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).toString(),"true", String.class);
		}).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).toString());
			return null;
		});
	}

	@Override
	public void updateCloseWindow(final PWindow window) {
		this.windows.remove(window);
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - закрытие окна");
			
			if(!this.isRunning)
				return;
			
			this.restTemplate.postForLocation(new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).toString(),"false", String.class);
		}).exceptionally(t -> {
			LOG.error("Сервер \"{}\" не доступен!", new StringBuilder(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).toString());
			return null;
		});
	}

	@Override
	public String getName() {
		return "Слушатель проводника";
	}
}