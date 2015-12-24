package ru.kolaer.client.javafx.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceControlManager {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceControlManager.class);
	
	private final List<Service> servicesList = new ArrayList<>();
	private final ExecutorService poolThreadServices = Executors.newScheduledThreadPool(2);
	
	public ServiceControlManager() {
		
	}
	
	public void runAllServices() {
		this.servicesList.parallelStream().forEach(this::runService);
	}
	
	public void runService(final Service service) {
		if(!service.isRunning()) {
			service.setRunningStatus(true);
			CompletableFuture.runAsync(() -> {
				try {
					service.run() ;
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	public void addService(final Service service) {
		this.servicesList.add(service);
	}
	
	public void addService(final Service service, boolean run) {
		this.addService(service);
		
		if(run) {
			this.runService(service);
		}			
	}
	
	public void removeService(final Service service){
		service.setRunningStatus(false);
		try {
			service.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.servicesList.remove(service);
	}
	
	public void removeAllServices() {
		this.servicesList.clear();
	}
}
