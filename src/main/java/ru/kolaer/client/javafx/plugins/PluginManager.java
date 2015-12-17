package ru.kolaer.client.javafx.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
		LOG.debug("Сканирование папки: \"{}\"", this.pathToPlugins);
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
				try {
					jarURL = jarFile.toURI().toURL();
				} catch(MalformedURLException e1) {
					LOG.error("Невозможно преобразовать в URL файл " + jarFile.getAbsolutePath() + "!", e1);
					return null;
				}
				
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
			    method.setAccessible(true);
			    method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{jarURL});

				try(final JarFile jarFileRead = new JarFile(jarFile)){

					final Enumeration<?> e = jarFileRead.entries();
					
					while(e.hasMoreElements()){
						final JarEntry je = (JarEntry) e.nextElement();
						LOG.trace("Файл({}) - {}", jarFileRead.getName(), je.getName());
						if(je.isDirectory() || !je.getName().endsWith(".class")){
							continue;
						}

						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');
						try {
							final Class<?> cls = this.getClass().getClassLoader().loadClass(className);
							if(cls.getAnnotation(ApplicationPlugin.class) != null){
								return (IKolaerPlugin) cls.newInstance();
							}
						} catch(Throwable ex){
							LOG.error("Невозможно прочитать класс: " + className, ex);
							continue;
						}
					}
				} catch(SecurityException secEx){
					LOG.error("Невозможно получить доступ к файлу " + jarFile.getAbsolutePath() + "!", secEx);
				} catch(IOException ioEx){
					LOG.error("Невозможно получить доступ к файлу " + jarFile.getAbsolutePath() + "!", ioEx);
				}
				return null;
			});
			resultInThreads.add(resultThread);
		}
		threadPoolForPlugins.shutdown();

		try {
			if(!threadPoolForPlugins.awaitTermination(10, TimeUnit.SECONDS)){
				LOG.error("Потоки прерваны. Истекло время ожидания!");
				final int countThreads = threadPoolForPlugins.shutdownNow().size();
				if(countThreads > 0)
					LOG.error("Не получено результатов с {} потоков.",countThreads);
			}
		} catch(InterruptedException e){
			LOG.warn("Потоки прерваны!", e);
		} finally{
			Thread.currentThread().setName("Добавление плагинов");
			for(Future<IKolaerPlugin> future : resultInThreads){
				try{
					IKolaerPlugin app = future.get();
					if(app != null) {
						result.add(app);
						LOG.info("Добвленно приложение: \"{}\"",app.getName());
					} else {
						LOG.warn("В .jar файле не обнаруженно плагина!");
					}
				}
				catch(InterruptedException | ExecutionException e){
					LOG.error("Ошибка при получении результата!", e);
				}
			}
		}
		return result;
	}
}