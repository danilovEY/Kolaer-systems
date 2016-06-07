package ru.kolaer.client.javafx.runnable;

import javafx.application.Application;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Launcher {
	private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

	public static final String pathToCache = System.getProperty("java.io.tmpdir") + "\\KolaerCache";
	public static final String pathToRunFile = pathToCache + "\\runnable.usa";

	public static void main(final String[] args) {
		if(!appIsRun()) {
			Platform.setImplicitExit(false);
			Application.launch(VMMainFrameImpl.class ,args);
		} else {
			LOG.warn("Приложение уже запущенно!");
			System.exit(0);
		}
	}
	private static boolean appIsRun() {
		final File pathToDir = new File(pathToCache);

		if(!pathToDir.exists())
			pathToDir.mkdirs();

		final File pathToFile = new File(pathToRunFile);

		if(pathToFile.exists()) {
			pathToFile.delete();
			
			try{
				TimeUnit.MILLISECONDS.sleep(300);
			}catch(InterruptedException e1){
				LOG.error("Прерывание операции!");
				return true;
			}
			
			if(pathToFile.exists()) {
				return true;
			}
		}

		return false;
	}
}