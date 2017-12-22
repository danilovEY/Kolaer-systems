package ru.kolaer.client.usa.runnable;

import javafx.application.Application;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.usa.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.client.usa.tools.Resources;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class Launcher {
	private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

	private static final String MULTIPLE_KEY = "--multiple";
	public static final String pathToCache = Resources.CACHE_PATH;
	public static final String pathToRunFile = pathToCache + "\\runnable.usa";
	public static final String pathToShowAppFile = pathToCache + "\\runnable_show.usa";

	public static void main(final String[] args) {
		if(containsKey(MULTIPLE_KEY, args) || !appIsRun()) {
			delete(new File(pathToRunFile));
			Platform.setImplicitExit(false);
			Application.launch(VMMainFrameImpl.class ,args);
		} else {
			LOG.warn("Приложение уже запущенно!");
			JOptionPane.showMessageDialog(null,
					"Приложение уже запущено.",
					"Внимание",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}

	private static boolean containsKey(String key, String[] args) {
		for (String arg : args) {
			if(arg.equals(key)) {
				return true;
			}
		}

		return false;
	}

	private static boolean delete(File pFile) {
		if(pFile.exists()) {
			if(pFile.isDirectory()) {
				final String[] strFiles = Optional.ofNullable(pFile.list())
						.orElse(new String[0]);

				for(String strFilename: strFiles) {
					File fileToDelete = new File(pFile, strFilename);

					delete(fileToDelete);
					try {
						Files.deleteIfExists(Paths.get(fileToDelete.getPath()));
					} catch (Throwable ignore) {
						return false;
					}
				}

			} else {
				return pFile.delete();
			}
		}

		return true;
	}

	private static boolean appIsRun() {
		final File pathToDir = new File(pathToCache);

		if(!pathToDir.exists())
			pathToDir.mkdirs();

		final File pathToFile = new File(pathToRunFile);

		if(pathToFile.exists()) {
			if(!pathToFile.delete()) {
				File pathToShowFile = new File(pathToShowAppFile);
				if(!pathToShowFile.exists()) {
					try {
						pathToShowFile.createNewFile();
					} catch (IOException e) {
						LOG.error("Невозможно создать файл: {}", pathToShowAppFile, e);
					}
				}
				return true;
			}
		}

		return false;
	}
}