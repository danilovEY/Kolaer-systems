package ru.kolaer.asmc.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ru.kolaer.asmc.ui.javafx.controller.CWebBrowser;

/**
 * Запускает задачу.
 * 
 * @author Danilov E.Y.
 *
 */
public class Application implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private String pathApp;

	public Application(String path) {
		this.pathApp = path;
	}

	public void start() {
		if (this.pathApp != null && !this.pathApp.equals(""))
			new Thread(this).start();
		else {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Не указан файл или ссылка!");
				alert.show();
			});

		}

	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

	public static String getOSVerion() {
		String os = System.getProperty("os.version");
		return os;
	}

	/**
	 * Проверка на правильность url-ссылки.
	 * 
	 * @param url
	 *            - Ссылка.
	 * @return - Возвращает true если это ссылка.
	 */
	public static boolean isURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	@Override
	public void run() {
		try {

			Runtime r = Runtime.getRuntime();

			if (isWindows()) {
				if (isURL(pathApp)) {
					if (SettingSingleton.getInstance().isDefaultWebBrowser()) {
						Platform.runLater(() -> {
							final CWebBrowser web = new CWebBrowser();
							web.show();
							web.load(pathApp);
						});
					} else {
						File simpleWebBrowser = new File(SettingSingleton.getInstance().getPathWebBrowser());
						if (simpleWebBrowser.exists() && simpleWebBrowser.isFile()) {
							r.exec(simpleWebBrowser.getAbsolutePath() + " \"" + this.pathApp + "\"");
						} else {
							Platform.runLater(() -> {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Ошибка!");
								alert.setHeaderText("Браузер \"" + this.pathApp + "\" не найден.");
								alert.setContentText("Запуск стандартного браузера...");
								alert.show();

								final CWebBrowser web = new CWebBrowser();
								web.show();
								web.load(pathApp);
							});
						}
					}
				} else {
					File file = new File(this.pathApp);
					if (file.exists()) {
						if (file.isDirectory()) {
							r.exec("explorer.exe \"" + this.pathApp + "\"");
						} else {
							r.exec("cmd /C \"" + this.pathApp + "\"");
						}
					} else {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Ошибка!");
							alert.setHeaderText("Файл \"" + this.pathApp + "\" не найден.");
							alert.setContentText("Если это ссылка, добавьте \"http://\"");
							alert.show();
						});
					}
				}
			}
		} catch (IOException e) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Не удалось запустить \"" + this.pathApp);
				alert.setContentText(e.getMessage());
				alert.show();
			});
		}
	}

	public String getPathApp() {
		return pathApp;
	}

	public void setPathApp(String pathApp) {
		this.pathApp = pathApp;
	}
}
