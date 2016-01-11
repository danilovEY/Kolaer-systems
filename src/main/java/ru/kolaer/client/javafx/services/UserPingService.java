package ru.kolaer.client.javafx.services;

import java.util.concurrent.TimeUnit;

import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.Resources;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class UserPingService implements Service {
	
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
		return "Ping";
	}

	@Override
	public void run() throws Exception {
		this.isRun = true;
		
		while(this.isRun){
			TimeUnit.SECONDS.sleep(5);
			String bool = restTemplate.getForObject(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/" + username + "/ping", String.class);
			System.out.println(bool);
		}
	}

	@Override
	public void stop() throws Exception {
		this.isRun = false;
	}

}
