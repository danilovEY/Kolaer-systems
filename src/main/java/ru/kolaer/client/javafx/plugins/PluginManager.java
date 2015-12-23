package ru.kolaer.client.javafx.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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

public class PluginManager {
	private static final Logger LOG = LoggerFactory.getLogger(PluginManager.class);

	private final String pathToPlugins;

	public PluginManager(final String pathToPlugins) {
		this.pathToPlugins = pathToPlugins;
	}

	public List<IKolaerPlugin> scanPlugins(final VMExplorer explorer) {

		LOG.debug("Сканирование папки: \"{}\"", this.pathToPlugins);
		final File dirToPlugins = new File(this.pathToPlugins);
		if(!dirToPlugins.isDirectory()){
			LOG.error("Путь: {} не является папкой или ее нет!", this.pathToPlugins);
			return Collections.emptyList();
		}

		final File[] jarFiles = dirToPlugins.listFiles((final File dir, final String name) -> {
			return name.endsWith(".jar") ? true : false;
		});

		CompletableFuture<List<IKolaerPlugin>> thread = CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setName("Интеграция class loader'ов в системный class loader");
			final List<Future<IKolaerPlugin>> futureList = new ArrayList<>();

			for(final File jarFile : jarFiles){
				final Future<IKolaerPlugin> resultFuture = Executors.newSingleThreadExecutor().submit(() -> {
					Thread.currentThread().setName("Поток для файла: " + jarFile.getName());
					IKolaerPlugin plugin = null;
					try(final JarFile jarFileRead = new JarFile(jarFile); 
							){
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
								if(cls.getAnnotation(ApplicationPlugin.class) != null){
									plugin = (IKolaerPlugin) cls.newInstance();
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
					return plugin;
				});
				futureList.add(resultFuture);
			}

			final List<IKolaerPlugin> result = new ArrayList<>();
			for(final Future<IKolaerPlugin> future : futureList){
				try{
					final IKolaerPlugin plg = future.get(10, TimeUnit.SECONDS);
					if(plg != null)
						result.add(plg);
				} catch(Exception e){
					LOG.error("Истекло время ожидания!");
					continue;
				}
			}
			return result;
		});

		try{
			return thread.get(30, TimeUnit.SECONDS);
		}catch(InterruptedException | ExecutionException | TimeoutException e){
			LOG.error("Потоки прерваны. Истекло время ожидания!");
			return Collections.emptyList();
		}
	}
}