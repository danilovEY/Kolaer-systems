package ru.kolaer.client.javafx.services;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;

import javax.ws.rs.core.MediaType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Служба для передачи IP пользователя на сервер.
 *
 * @author danilovey
 * @version 0.1
 */
public class ServiceUserIpAndHostName implements Service {
	private final Logger LOG = LoggerFactory.getLogger(ServiceUserIpAndHostName.class);
	/**Имя пользователя.*/
	private final String username = System.getProperty("user.name");
	private boolean isRun = false;
	/**Объект для взаимодействия с сервером.*/
	private final WebResource webResource;

	public ServiceUserIpAndHostName(final WebResource webResource) {
		this.webResource = webResource.path("user").path(username);
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Передача IP и имя компьютера");
		this.isRun = true;

		while(this.isRun) {
			try {
				final InetAddress inet = InetAddress.getLocalHost();
				this.webResource.path("ip").entity(inet.getHostAddress(), MediaType.APPLICATION_JSON).post();
				this.webResource.path("hostname").entity(inet.getHostName(), MediaType.APPLICATION_JSON).post();
				TimeUnit.HOURS.sleep(2);
			} catch(final UniformInterfaceException | ClientHandlerException ex) {
				LOG.error("URL: не доступен!", ex);
			} catch(final UnknownHostException e) {
				LOG.error("Ошибка при получении IP!", e);
			} catch(InterruptedException e) {
				LOG.error("Прерыване задержки потока!");
			}
		}
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
