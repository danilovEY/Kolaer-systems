package ru.kolaer.client.javafx.services;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class ServiceControlManager {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceControlManager.class);
	private final List<Service> servicesList = new LinkedList<>();
	private final ExecutorService readPluginsThread = Executors.newCachedThreadPool();
	private boolean autoRun = false;
	
	public ServiceControlManager() {
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.springframework.web.client.RestTemplate")).setLevel(Level.INFO);
	}
	
	public void runAllServices() {
		this.servicesList.parallelStream().forEach(this::runService);
	}
	
	public void runService(final Service service) {
		LOG.info("Запуск службы: \"{}\"", service.getName());
		service.setRunningStatus(true);
		CompletableFuture.runAsync(service, readPluginsThread).exceptionally(t -> {
			LOG.error("Ошибка в запуске службы!", t);
			return null;
		});	
	}
	
	public void addService(final Service service) {
		this.servicesList.add(service);
		
		if(this.autoRun && !service.isRunning())
			this.runService(service);
	}
	
	public void addService(final Service service, final boolean run) {
		this.servicesList.add(service);
		
		if((run || this.autoRun) && !service.isRunning()) {
			this.runService(service);
		}			
	}
	
	public void removeService(final Service service){
		service.setRunningStatus(false);
		service.stop();
		this.servicesList.remove(service);
	}
	
	public void removeAllServices() {
		this.servicesList.parallelStream().forEach(service -> { 
			service.setRunningStatus(false);
			service.stop();
		});
		this.servicesList.clear();
	}
	
	public void setAutoRun(final boolean autoRun) {
		this.autoRun = autoRun;
	}
	
	public boolean isAutoRun() {
		return this.autoRun;
	}
	
	public List<Service> getServices() {
		return this.servicesList;
	}
}
