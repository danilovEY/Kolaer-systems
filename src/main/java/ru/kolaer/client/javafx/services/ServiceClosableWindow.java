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
		while(true && this.isRunning) {
			RestTemplate restTemplate = new RestTemplate();
			try {
				System.out.println(restTemplate.getForObject("http://localhost:8080/kolaer/api/0.0.1/system/windows/close", String.class));
			} catch(RestClientException ex) {
				ex.printStackTrace();
			}
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