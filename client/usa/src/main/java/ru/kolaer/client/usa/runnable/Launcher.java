package ru.kolaer.client.usa.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.TypeUi;
import ru.kolaer.client.usa.mvp.view.awt.AwtUiRunner;
import ru.kolaer.client.usa.mvp.view.javafx.JavaFxUiRunner;
import ru.kolaer.client.usa.mvp.view.swing.SwingUiRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Launcher {
	private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

	private static final String applicationUiTypeKey = "--ui";
	private static final String applicationUiTypeLow = "low";
	private static final String applicationUiTypeMedium = "medium";
	private static final String applicationUiTypeHigh = "high";
	public static final String pathToCache = System.getProperty("java.io.tmpdir") + "\\KolaerCache";
	public static final String pathToRunFile = pathToCache + "\\runnable.usa";
	public static final String pathToShowAppFile = pathToCache + "\\runnable_show.usa";

	public static void main(final String[] args) {
		//if(!appIsRun()) {
		//delete(new File(pathToCache));

		TypeUi typeUi = getTypeUi(args);

		if(typeUi == TypeUi.LOW) {
			new AwtUiRunner().run(args);
		} else if(typeUi == TypeUi.MEDIUM) {
			new SwingUiRunner().run(args);
		} else {
			new JavaFxUiRunner().run(args);
		}


		//} else {
		//	LOG.warn("Приложение уже запущенно!");
		//	System.exit(0);
		//}
	}

	private static TypeUi getTypeUi(String[] args) {
		if(args != null) {
			for (String arg : args) {
				if(arg.contains(applicationUiTypeKey)) {
					if(arg.contains(applicationUiTypeHigh)) {
						return TypeUi.HIGH;
					} if(arg.contains(applicationUiTypeLow)) {
						return TypeUi.LOW;
					} if(arg.contains(applicationUiTypeMedium)) {
						return TypeUi.MEDIUM;
					} else {
						return TypeUi.HIGH;
					}
				}
			}
		}

		return TypeUi.HIGH;
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
				pFile.delete();
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
			final File pathToShowFile = new File(pathToShowAppFile);
			if(!pathToShowFile.exists()) {
				try {
					pathToShowFile.createNewFile();
				} catch (IOException e) {
					LOG.error("Невозможно создать файл: {}", pathToShowAppFile, e);
				}
			} else {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					return true;
				}
				if(pathToShowFile.exists()) {
					pathToFile.delete();
					pathToShowFile.delete();
					return false;
				}
			}
			return true;
		}

		return false;
	}
}