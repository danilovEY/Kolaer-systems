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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginLoader {
	private static final Logger LOG = LoggerFactory.getLogger(PluginLoader.class);

	private final String pathToPlugins;

	public PluginLoader(final String pathToPlugins) {
		this.pathToPlugins = pathToPlugins;
	}

	public List<IKolaerPlugin> scanPlugins() {
		final File dirToPlugins = new File(this.pathToPlugins);
		if(!dirToPlugins.isDirectory()){
			LOG.error("Путь: {} не является папкой или ее нет!", this.pathToPlugins);
			return Collections.emptyList();
		}

		final List<IKolaerPlugin> result = new ArrayList<>();
		for(final File jarFile : dirToPlugins.listFiles((File dir, String name) -> {
			return name.endsWith(".jar") ? true : false;
		})){

			URL jarURL = null;
			try{
				jarURL = jarFile.toURI().toURL();
			}
			catch(MalformedURLException e1){
				LOG.error("Невозможно преобразовать в URL файл " + jarFile.getAbsolutePath() + "!", e1);
			}

			try(final URLClassLoader classLoader = new URLClassLoader(new URL[] {
			        jarURL }); final JarFile jarFileRead = new JarFile(jarFile);){

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
								LOG.info("Добавлен плагин: {}", app.getName());
								result.add(app);
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
				continue;
			}
			catch(IOException ioEx){
				LOG.error("Невозможно получить доступ к файлу " + jarFile.getAbsolutePath() + "!", ioEx);
				continue;
			}
		}
		return result;
	}
}
