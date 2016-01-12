package ru.kolaer.client.javafx.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.Resources;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class UserPingService implements Service {
	private final Logger LOG = LoggerFactory.getLogger(UserPingService.class);
	private final RestTemplate restTemplate = new RestTemplate();
	private final String username = System.getProperty("user.name");
	private boolean isRun = false;
	
	@Override
	public void setRunningStatus(final boolean isRun) {
		this.isRun = isRun;
	}

	@Override
	public boolean isRunning() {
		return this.isRun;
	}

	@Override
	public String getName() {
		return "Ping Service";
	}

	@Override
	public void run() {
		this.isRun = true;
		Thread.currentThread().setName("Прием и передача пинга");
		while(this.isRun){
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				String bool = restTemplate.getForObject(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/" + username + "/ping", String.class);
				
				if(bool.equals("false")){
					restTemplate.postForObject(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/" + username + "/ping", "true", String.class);
				}
			} catch(RestClientException ex) {
				LOG.error("Сервер \"{}\" не доступен!", Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/" + username + "/ping");
			}
		}
	}

	@Override
	public void stop() {
		this.isRun = false;
	}
}
