package ru.kolaer.asmc.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Запускает задачу.
 * 
 * @author Danilov E.Y.
 *
 */
public class Application implements Runnable {
	/**Путь к запускающему файлу.*/
	private String pathApp;
	/**Путь к программу с помощью чего открывать пользОткрывать с мо*/
	private String openWith;
	/**Поток для запуска приложения пользователя.*/
	private final ExecutorService threadForRunApp = Executors.newSingleThreadExecutor();
	
	public Application(final String path, final String openWith) {
		this.pathApp = path;
		this.openWith = openWith;
	}

	public void start() {
		if (this.pathApp != null && !this.pathApp.equals("")) {
			CompletableFuture.runAsync(this, threadForRunApp).exceptionally(t -> {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Ошибка!");
					alert.setHeaderText(t.getMessage());
					alert.show();
				});
				return null;
			});
			this.threadForRunApp.shutdown();
		} else {
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
	public static boolean isURL(final String url) {
		try {
			new URL(url);
			return true;
		} catch (final MalformedURLException e) {
    		switch(url.substring(url.lastIndexOf('.'))) {
    			case ".html" : return true;
    			case ".php" : return true;
    			default : return false;
    		}
		}
	}

	@Override
	public void run() {
		try {
			final Runtime r = Runtime.getRuntime();
			
			if (Application.isWindows()) {
				if (Application.isURL(pathApp)) {
					String pathWeb = "";
					if(SettingSingleton.getInstance().isAllLabels()) {
						if (SettingSingleton.getInstance().isDefaultWebBrowser()) {
							Platform.runLater(() -> {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Ошибка!");
								alert.setHeaderText("Стандартный браузер больше не поддерживается.");
								alert.show();
							});
							return;
						} else if(SettingSingleton.getInstance().isDefaultUserWebBrowser()) {
							r.exec("explorer \"" + this.pathApp + "\"");
							return;
						} else {
							pathWeb = SettingSingleton.getInstance().getPathWebBrowser();
						}
					} else {
						pathWeb = this.openWith;
					}
					
					final File simpleWebBrowser = new File(pathWeb);
					if (simpleWebBrowser.exists() && simpleWebBrowser.isFile()) {
						r.exec(simpleWebBrowser.getAbsolutePath() + " \"" + this.pathApp + "\"");
					} else {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Ошибка!");
							alert.setHeaderText("Браузер \"" + simpleWebBrowser.getAbsolutePath() + "\" не найден.");
							alert.show();
						});
					}
				} else {		
					final File file = new File(this.pathApp);
					
					if (file.exists()) {	
						if(this.openWith!= null && !this.openWith.isEmpty()) {
							r.exec(this.openWith + " " + this.pathApp);
						} else {
							if (file.isDirectory()) {
								r.exec("explorer.exe \"" + this.pathApp + "\"");
							} else {
								r.exec("cmd /C \"" + this.pathApp + "\"");
							}
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
		} catch (final IOException e) {
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

	public void setPathApp(final String pathApp) {
		this.pathApp = pathApp;
	}
}
