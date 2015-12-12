package ru.kolaer.client.javafx.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManager {
	private static final Logger LOG = LoggerFactory.getLogger(PluginManager.class);

	private final String pathToPlugins;

	public PluginManager(final String pathToPlugins) {
		this.pathToPlugins = pathToPlugins;
	}

	public List<IKolaerPlugin> scanPlugins() {
		final File dirToPlugins = new File(this.pathToPlugins);
		if(!dirToPlugins.isDirectory()){
			LOG.error("Путь: {} не является папкой или ее нет!", this.pathToPlugins);
			return Collections.emptyList();
		}

		final List<IKolaerPlugin> result = new ArrayList<>();

		File[] jarFiles = dirToPlugins.listFiles((File dir, String name) -> {
			return name.endsWith(".jar") ? true : false;
		});

		ExecutorService threadPoolForPlugins = null;
		List<Future<IKolaerPlugin>> resultInThreads = new ArrayList<>();
		if(jarFiles.length > 0){
			threadPoolForPlugins = Executors.newFixedThreadPool(jarFiles.length);
		}

		for(final File jarFile : jarFiles){
			Future<IKolaerPlugin> resultThread = threadPoolForPlugins.submit(() -> {
				Thread.currentThread().setName("Поток для файла: " + jarFile.getName());
				
				URL jarURL = null;
				try{
					jarURL = jarFile.toURI().toURL();
				}
				catch(MalformedURLException e1){
					LOG.error("Невозможно преобразовать в URL файл " + jarFile.getAbsolutePath() + "!", e1);
				}
				
				final URLClassLoader classLoader = new URLClassLoader(new URL[] {jarURL }, Thread.currentThread().getContextClassLoader());
				
				Thread.currentThread().setContextClassLoader(classLoader);
				
				try( final JarFile jarFileRead = new JarFile(jarFile);){

					final Enumeration<?> e = jarFileRead.entries();

					String packageName = "";

					while(e.hasMoreElements()){
						final JarEntry je = (JarEntry) e.nextElement();

						if(je.isDirectory() || !je.getName().endsWith(".class")){
							continue;
						}

						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');
						if(className.startsWith(packageName)){
							try{
								final Class<?> cls = classLoader.loadClass(className);
								if(cls.getAnnotation(ApplicationPlugin.class) != null){
									final IKolaerPlugin app = (IKolaerPlugin) cls.newInstance();
									return app;
								}
							}
							catch(Throwable ex){
								LOG.error("Невозможно прочитать класс:" + className, ex);
								continue;
							}
						}
					}
				}
				catch(SecurityException secEx){
					LOG.error("Невозможно получить доступ к файлу " + jarFile.getAbsolutePath() + "!", secEx);
				}
				catch(IOException ioEx){
					LOG.error("Невозможно получить доступ к файлу " + jarFile.getAbsolutePath() + "!", ioEx);
				}
				return null;
			});
			resultInThreads.add(resultThread);
		}
		threadPoolForPlugins.shutdown();

		try{
			if(!threadPoolForPlugins.awaitTermination(5, TimeUnit.SECONDS)){
				LOG.error("Потоки прерваны. Истекло время ожидания!");
				int countThreads = threadPoolForPlugins.shutdownNow().size();
				if(countThreads > 0)
					LOG.error("Не получено результатов с {} потоков.",countThreads);
			}
		}
		catch(InterruptedException e){
			LOG.warn("Потоки прерваны!", e);
		}
		finally{
			Thread.currentThread().setName("Добавление плагинов");
			for(Future<IKolaerPlugin> future : resultInThreads){
				try{
					IKolaerPlugin app = future.get();
					result.add(app);
					LOG.info(app.getName());
				}
				catch(InterruptedException | ExecutionException e){
					LOG.error("Ошибка при получении результата!", e);
				}
			}
		}
		return result;
	}
}
