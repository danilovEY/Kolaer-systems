package ru.kolaer.client.javafx.services;

import java.util.concurrent.TimeUnit;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

public class ServiceClosableWindow implements Service {

	private final VMExplorer explorer;
	private boolean isRunning = false;
	
	public ServiceClosableWindow(final VMExplorer explorer) {
		this.explorer = explorer;
	}
	
	@Override
	public void run() throws Exception {
		final String username = System.getProperty("user.name");
		while(true && this.isRunning) {
			RestTemplate restTemplate = new RestTemplate();
			try {
				restTemplate.postForLocation("http://localhost:8080/kolaer/system/user/"+ username + "/window/ASUP",null);
			} catch(RestClientException ex) {
				ex.printStackTrace();
			}
			System.exit(0);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public void stop() throws Exception {
		this.isRunning = false;
	}
}