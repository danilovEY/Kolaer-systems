package ru.kolaer.asmc.tools;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Запускает задачу.
 * 
 * @author Danilov E.Y.
 *
 */
public class Application implements Runnable {
	private final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	/**Путь к запускающему файлу.*/
	private String pathApp;
	/**Путь к программу с помощью чего открывать пользОткрывать с мо*/
	private String openWith;
	/**Поток для запуска приложения пользователя.*/
	private final ExecutorService threadForRunApp = Executors.newSingleThreadExecutor();
	private final UniformSystemEditorKit editorKit;
	
	public Application(final String path, final String openWith, final UniformSystemEditorKit editorKit) {
		this.pathApp = path;
		this.openWith = openWith;
		this.editorKit = editorKit;
	}

	public void start() {
		if (this.pathApp != null && !this.pathApp.equals("")) {
			CompletableFuture.runAsync(this, threadForRunApp).exceptionally(t -> {
				LOG.error("Неудалось запустить: {}", this.pathApp, t);
				return null;
			});
			this.threadForRunApp.shutdown();
		} else {
			this.editorKit.getUISystemUS().getDialog().createErrorDialog("Ошибка!", "Не указан файл или ссылка!").show();
		}
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isWindowsXP() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("xp") >= 0);
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
			if(url.length()>4) {
				switch(url.substring(url.length() - 3)) {
	    			case ".html" : return true;
	    			case ".php" : return true;
	    			default : return false;
	    		}
			} else {
				return false;
			}
		}
	}

	private void operUrlInPluginBrowser(final String url) {
		for(final UniformSystemPlugin plugin : this.editorKit.getPluginsUS().getPlugins()) {
			if(this.editorKit.getPluginsUS().getNamePlugin(plugin).equals("Браузер")) {
				this.editorKit.getPluginsUS().showPlugin(plugin);
				break;
			}
		}
		
		this.editorKit.getPluginsUS().notifyPlugins("url", url);
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
							this.operUrlInPluginBrowser(this.pathApp);
							return;
						} else if(SettingSingleton.getInstance().isDefaultUserWebBrowser()) {
							if(isWindowsXP()) {
								this.operUrlInPluginBrowser(this.pathApp);
							} else {
								r.exec("cmd /C START \"\" \"" + this.pathApp + "\"");
							}
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
								r.exec("cmd /C explorer \"" + this.pathApp + "\"");
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
