package ru.kolaer.client.javafx.services;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.Resources;
import ru.kolaer.server.dao.entities.PackageNetwork;

public class ServiceScreen implements Service {
	private Logger LOG = LoggerFactory.getLogger(ServiceScreen.class);
	private boolean isRun = false;
	/** Имя пользователя. */
	private final String username = System.getProperty("user.name");

	@Override
	public void run() {
		try{
			TimeUnit.SECONDS.sleep(20);
		}catch(final InterruptedException e1){
			LOG.error("Превышено ожидание", e1);
			this.isRun = false;
			return;
		}
		while(this.isRun){
			try{
				final Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				final BufferedImage capture = new Robot().createScreenCapture(screenRect);

				final RestTemplate restTemplate = new RestTemplate();
				byte[] imageInByte;

				try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()){
					ImageIO.write(capture, "jpg", baos);
					baos.flush();
					imageInByte = baos.toByteArray();
					final PackageNetwork packageNetwork = new PackageNetwork(imageInByte, true);
					restTemplate.postForObject("http://" + Resources.URL_TO_KOLAER_RESTFUL.toString() + "/system/user/" + username + "/package/screen", packageNetwork, PackageNetwork.class);
				}catch(final IOException e){
					LOG.error("Невозможно записать скриншот!", e);
				}catch(final RestClientException ex){
					LOG.error("Невозможно отправить скриншот!", ex);
				}
			}catch(final AWTException e){
				LOG.error("Невозможно сделать скриншот!", e);
			}

			try{
				TimeUnit.MINUTES.sleep(3);
			}catch(final InterruptedException e1){
				LOG.error("Превышено ожидание", e1);
				this.isRun = false;
				return;
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
		return "Служба передачи скриншотов";
	}

	@Override
	public void stop() {

	}

}
