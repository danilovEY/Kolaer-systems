package ru.kolaer.client.javafx.plugins;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

/**
 * Чтение плагинов в виде .jar файлов.
 *
 * @author danilovey
 * @version 0.1
 */
public class PluginReader {
	private final Logger LOG = LoggerFactory.getLogger(PluginReader.class);
	/**Путь к папке с плагинами.*/
	private final String pathToPlugins;

	public PluginReader(final String pathToPlugins) {
		this.pathToPlugins = pathToPlugins;
	}
	/**
	 * Сканирование папки с плагинами.
	 * @param explorer - Ezplorer в который добавляются плагины.
	 * @return - Список плагинов.
	 */
	public List<UniformSystemPlugin> scanPlugins(final VMExplorer explorer) {
		LOG.debug("Сканирование папки: \"{}\"", this.pathToPlugins);
		final File dirToPlugins = new File(this.pathToPlugins);
		if(!dirToPlugins.isDirectory()){
			LOG.error("Путь: {} не является папкой или ее нет!", this.pathToPlugins);
			return Collections.emptyList();
		}

		final File[] jarFiles = dirToPlugins.listFiles((final File dir, final String name) -> {
			return name.endsWith(".jar") ? true : false;
		});

		CompletableFuture<List<UniformSystemPlugin>> thread = CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setName("Интеграция class loader'ов в системный class loader");
			final List<Future<UniformSystemPlugin>> futureList = new ArrayList<>();

			for(final File jarFile : jarFiles){
				final Future<UniformSystemPlugin> resultFuture = Executors.newSingleThreadExecutor().submit(() -> {
					Thread.currentThread().setName("Поток для файла: " + jarFile.getName());

					try(final JarFile jarFileRead = new JarFile(jarFile)){
						final URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, ClassLoader.getSystemClassLoader());
						final Enumeration<?> e = jarFileRead.entries();

						while(e.hasMoreElements()){
							final JarEntry je = (JarEntry) e.nextElement();
							LOG.trace("Файл({}) - {}", jarFileRead.getName(), je.getName());
							if(je.isDirectory() || !je.getName().endsWith(".class")){
								continue;
							}

							String className = je.getName().substring(0, je.getName().length() - 6);
							className = className.replace('/', '.');
							try{
								final Class<?> cls = Class.forName(className, false, jarClassLoader);
								if(cls.getAnnotation(UniformSystem.class) != null){
									final UniformSystemPlugin plugin = (UniformSystemPlugin) cls.newInstance();
									explorer.addPlugin(plugin,jarClassLoader);
									LOG.info("Добавлено приложение: \"{}\"", plugin.getName());
									return plugin;
								}
							} catch(Throwable ex){
								LOG.error("Невозможно прочитать класс: " + className, ex);
								continue;
							}
						}
					}catch(SecurityException secEx){
						LOG.error("Невозможно получить доступ к файлу \"" + jarFile.getAbsolutePath() + "\"!", secEx);
					}catch(IOException ioEx){
						LOG.error("Невозможно прочитать файл \"" + jarFile.getAbsolutePath() + "\"!", ioEx);
					}
					return null;
				});
				futureList.add(resultFuture);
			}

			final List<UniformSystemPlugin> result = new ArrayList<>();
			for(final Future<UniformSystemPlugin> future : futureList){
				try{
					final UniformSystemPlugin plg = future.get(1, TimeUnit.MINUTES);
					if(plg != null)
						result.add(plg);
				} catch(final Exception e){
					LOG.error("Истекло время ожидания!");
					continue;
				}
			}
			return result;
		});

		try{
			return thread.get(5, TimeUnit.MINUTES);
		}catch(InterruptedException | ExecutionException | TimeoutException e){
			LOG.error("Потоки прерваны. Истекло время ожидания!", e);
			return Collections.emptyList();
		}
	}
}