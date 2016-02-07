package ru.kolaer.client.javafx.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.Resources;

public class SeviceUserIP implements Service {
	private final Logger LOG = LoggerFactory.getLogger(SeviceUserIP.class);
	private final String username = System.getProperty("user.name");
	private boolean isRun = false;
	private final RestTemplate rest = new RestTemplate();
	
	@Override
	public void run() {
		Thread.currentThread().setName("Передача IP");
		while(this.isRun) {
			
			final StringBuilder url = new StringBuilder("http://").append(Resources.URL_TO_KOLAER_RESTFUL.toString())
					.append("/system/user/")
					.append(this.username)
					.append("/ip");
			try {
				TimeUnit.MINUTES.sleep(1);
				final InetAddress inet = InetAddress.getLocalHost();
				rest.postForObject(url.toString(), inet.getHostAddress(), String.class);
				TimeUnit.HOURS.sleep(2);
			} catch(final RestClientException ex) {
				LOG.error("URL: \"{}\" не доступен!", url.toString());
			} catch(final UnknownHostException e) {
				LOG.error("Ошибка при получении IP!", e);
			} catch(InterruptedException e) {
				LOG.error("Прерыване задержки потока!");
			}
		}
	}

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
		return "Передача IP";
	}

	@Override
	public void stop() {
		this.isRun = false;
	}
}
