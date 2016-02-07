package ru.kolaer.client.javafx.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class ServiceControlManager {
	private final Logger LOG = LoggerFactory.getLogger(ServiceControlManager.class);
	private final Map<Service, Future<Void>> runnableService = new HashMap<>();
	private final ExecutorService readPluginsThread = Executors.newCachedThreadPool();
	private boolean autoRun = false;
	
	public ServiceControlManager() {
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.springframework.web.client.RestTemplate")).setLevel(Level.INFO);
	}
	
	public void runAllServices() {
		this.runnableService.keySet().parallelStream().forEach(this::runService);
	}
	
	public void runService(final Service service) {
		if(!service.isRunning()) {
			LOG.info("Запуск службы: \"{}\"", service.getName());
			service.setRunningStatus(true);
			final Future<Void> futureService = CompletableFuture.runAsync(service, readPluginsThread).exceptionally(t -> {
				LOG.error("Ошибка в запуске службы!", t);
				return null;
			});	
			this.runnableService.put(service, futureService);
			
		}
	}
	
	public void addService(final Service service) {
		this.addService(service, this.autoRun);
	}
	
	public void addService(final Service service, final boolean run) {
		this.runnableService.put(service, null);
		
		LOG.info("Добавлена служба: {}", service.getName());
		
		if((run || this.autoRun) && !service.isRunning()) {
			this.runService(service);
		}			
	}
	
	public void removeService(final Service service){
		if(service != null) {
			service.setRunningStatus(false);
			service.stop();
			
			final Future<Void> future = this.runnableService.get(service);
			if(future != null) {
				try{
					future.get(10, TimeUnit.SECONDS);
				} catch(final Exception ex) {
					future.cancel(true);
				}
			}
		}
	}
	
	public void removeAllServices() {
		this.runnableService.entrySet().forEach(entity -> {
			final Service service = entity.getKey();
			service.setRunningStatus(false);
			service.stop();
			try {
				entity.getValue().get(10, TimeUnit.SECONDS);
			} catch(final Exception ex) {
				entity.getValue().cancel(true);
			}
		});

		this.runnableService.clear();
	}
	
	public void setAutoRun(final boolean autoRun) {
		this.autoRun = autoRun;
	}
	
	public boolean isAutoRun() {
		return this.autoRun;
	}
	
	public Set<Service> getServices() {
		return this.runnableService.keySet();
	}
}
