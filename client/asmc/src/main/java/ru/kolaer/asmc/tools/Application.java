package ru.kolaer.asmc.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.PluginsUS;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
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
				this.editorKit.getUISystemUS().getNotification()
						.showErrorNotifi("Ошибка!", "Неудалось запустить: " + this.pathApp);
				LOG.error("Неудалось запустить: {}", this.pathApp, t);
				return null;
			});
			this.threadForRunApp.shutdown();
		} else {
			this.editorKit.getUISystemUS().getNotification()
					.showErrorNotifi("Ошибка!", "Не указан файл или ссылка!");
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

	private void openUrlInPluginBrowser(String url) {
		PluginsUS<UniformSystemPlugin> pluginsUS = editorKit.getPluginsUS();
		Collection<UniformSystemPlugin> plugins = pluginsUS.getPlugins();
		for(UniformSystemPlugin plugin : plugins) {
			if(pluginsUS.getNamePlugin(plugin).equals("Браузер")) {
				pluginsUS.showPlugin(plugin);
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
					if(this.openWith == null || this.openWith.trim().isEmpty()) {
						if (isWindowsXP()) {
							this.openUrlInPluginBrowser(this.pathApp);
						} else {
							r.exec("cmd /C START \"\" \"" + this.pathApp + "\"");
						}
					} else {
						final File simpleWebBrowser = new File(this.openWith);
						if (simpleWebBrowser.exists() && simpleWebBrowser.isFile()) {
							r.exec(simpleWebBrowser.getAbsolutePath() + " \"" + this.pathApp + "\"");
						} else {
							this.editorKit.getUISystemUS().getNotification()
									.showErrorNotifi("Ошибка!", "Браузер не найден!");
						}
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
						this.editorKit.getUISystemUS().getNotification()
								.showErrorNotifi("Ошибка!", "Файл \"" + this.pathApp + "\" не найден.");
					}
				}
			}
		} catch (final IOException e) {
			this.editorKit.getUISystemUS().getNotification()
					.showErrorNotifi("Ошибка!", "Не удалось запустить \"" + this.pathApp);
		}
	}
}
