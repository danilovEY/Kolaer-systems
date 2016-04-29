package ru.kolaer.client.javafx.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.javafx.tools.Resources;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Служба для передачи IP пользователя на сервер.
 *
 * @author danilovey
 * @version 0.1
 */
public class SeviceUserIpAndHostName implements Service {
	private final Logger LOG = LoggerFactory.getLogger(SeviceUserIpAndHostName.class);
	/**Имя пользователя.*/
	private final String username = System.getProperty("user.name");
	private boolean isRun = false;
	/**Объект для взаимодействия с сервером.*/
	//private final RestTemplate rest = new RestTemplate();
	
	@Override
	public void run() {
		Thread.currentThread().setName("Передача IP и имя компьютера");
		this.isRun = true;
		/*while(this.isRun) {
			
			final String url = "http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/" + this.username;
					
			try {
				TimeUnit.SECONDS.sleep(5);
				final InetAddress inet = InetAddress.getLocalHost();
				rest.postForObject(url + "/ip", inet.getHostAddress(), String.class);
				rest.postForObject(url + "/hostname", inet.getHostName(), String.class);
				TimeUnit.HOURS.sleep(2);
			} catch(final RestClientException ex) {
				LOG.error("URL: \"{}\" не доступен!", url.toString());
			} catch(final UnknownHostException e) {
				LOG.error("Ошибка при получении IP!", e);
			} catch(InterruptedException e) {
				LOG.error("Прерыване задержки потока!");
			}
		}*/
	}

	@Override
	public boolean isRunning() {
		return this.isRun;
	}

	@Override
	public String getName() {
		return "Передача IP и имя компьютера";
	}

	@Override
	public void stop() {
		this.isRun = false;
	}
}
