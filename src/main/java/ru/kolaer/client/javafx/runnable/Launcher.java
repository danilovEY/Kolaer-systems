package ru.kolaer.client.javafx.runnable;

import javafx.application.Application;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;

import java.io.*;

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
			int status = 0;
			try(FileInputStream fileOutputStream = new FileInputStream(pathToFile)) {
				status = fileOutputStream.read();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(status == 1) {
				pathToFile.delete();
				return false;
			}

			try (FileOutputStream fileOutputStream = new FileOutputStream(pathToFile)) {
				fileOutputStream.write(1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}

		return false;
	}
}