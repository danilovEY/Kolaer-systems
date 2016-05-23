package ru.kolaer.client.javafx.services;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.javafx.tools.Resources;

import java.util.concurrent.TimeUnit;

/**
 * Служба для пинга сервера (чтобы сервер автоматически не удалил подключенного пользователя).
 * @author Danilov
 * @version 0.1
 */
public class UserPingService implements Service {
	private final Logger LOG = LoggerFactory.getLogger(UserPingService.class);
	/**Объект для взаимодействия с сервером.*/
	private final WebResource webResource;
	/**Имя пользователя.*/
	private final String username = System.getProperty("user.name");
	private boolean isRunning = false;

	public UserPingService(final WebResource webResource) {
		this.webResource = webResource;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
	public String getName() {
		return "Ping Service";
	}

	@Override
	public void run() {
		this.isRunning = true;
		Thread.currentThread().setName("Прием и передача пинга");
		while(this.isRunning){
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				LOG.error("Ошибка!", e);
				this.isRunning = false;
				return;
			}
			try {
				//Получаем статус пинга пользователя.
				final String bool = this.webResource.path("user").path(username).path("ping").get(String.class);
				//Если false, значит серверу нужен наш пинг.
				if(bool.equals("false")){
					this.webResource.path("user").path(username).path("ping").entity("true").post();
				}
			} catch(UniformInterfaceException | ClientHandlerException ex) {
				LOG.error("Сервер \"{}\" не доступен!", "http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/" + username + "/ping");
			}
		}
	}

	@Override
	public void stop() {
		this.isRunning = false;
	}
}
