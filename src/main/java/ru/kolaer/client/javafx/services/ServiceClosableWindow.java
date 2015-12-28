package ru.kolaer.client.javafx.services;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

public class ServiceClosableWindow implements Service, ExplorerObserver {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceClosableWindow.class);
	private final RestTemplate restTemplate = new RestTemplate();	
	private final String username = System.getProperty("user.name");
	private final VMExplorer explorer;
	private boolean isRunning = false;
	
	public ServiceClosableWindow(final VMExplorer explorer) {
		this.explorer = explorer;
		this.explorer.registerObserver(this);
	}
	
	@Override
	public void run() throws Exception {
		
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
	public void stop() throws Exception {
		this.isRunning = false;
		this.explorer.removeObserver(this);
	}

	@Override
	public void updateOpenWindow(PWindow window) {
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - открытие окна");
			
			if(!this.isRunning)
				return;
			
			restTemplate.postForLocation(new StringBuilder("http://localhost:8080/kolaer/system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).toString(),"true", String.class);
		}).exceptionally(t -> {
			LOG.error("Не удается отправить данные!", t);
			return null;
		});
	}

	@Override
	public void updateCloseWindow(PWindow window) {
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName(this.getName() + ": отправка данных - закрытие окна");
			
			if(!this.isRunning)
				return;
			
			restTemplate.postForLocation(new StringBuilder("http://localhost:8080/kolaer/system/user/").append(username).append("/window/").append(window.getApplicationModel().getName()).toString(),"false", String.class);
		}).exceptionally(t -> {
			LOG.error("Не удается отправить данные!", t);
			return null;
		});
	}

	@Override
	public String getName() {
		return "Слушатель проводника";
	}
}