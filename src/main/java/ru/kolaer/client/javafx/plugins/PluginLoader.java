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

public class PluginLoader {
	private final String pathToPlugins;

	public PluginLoader(final String pathToPlugins) {
		this.pathToPlugins = pathToPlugins;
	}

	public List<IKolaerPlugin> scanPlugins() {
		File dirToPlugins = new File(this.pathToPlugins);
		List<IKolaerPlugin> result = new ArrayList<>();
		try {
			for (File jarFile : dirToPlugins.listFiles((File dir, String name) -> {
				return name.endsWith(".jar") ? true : false;
			})) {
	
				URL jarURL = jarFile.toURI().toURL();
	
	
				final URLClassLoader classLoader = new URLClassLoader(new URL[] { jarURL });
	
				JarFile jarFileRead = new JarFile(jarFile);
				
				Enumeration<?> e = jarFileRead.entries();
	
				List<String> classList = new ArrayList<>();
				String packageName = "";
				while (e.hasMoreElements()) {
					JarEntry je = (JarEntry) e.nextElement();
	
					if (je.isDirectory() || !je.getName().endsWith(".class")) {
						if (je.getName().endsWith(".plugin"))
							packageName = je.getName().substring(0, je.getName().length() - ".plugin".length());
						continue;
					}
	
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replace('/', '.');
					if (className.startsWith(packageName)) {
						System.out.println(packageName);
						classList.add(className);
					}
				}
	
				for (int i = 0; i < classList.size(); i++) {
					try {
						System.out.println(classList.get(i));
						Class<?> cls = null;
	
						cls = classLoader.loadClass(classList.get(i));
						if (cls.getAnnotation(ApplicationPlugin.class) != null) {
							System.out.println("AAAA");
							IKolaerPlugin app = (IKolaerPlugin) cls.newInstance();
							System.out.println(app.getLabel().getName());
							result.add(app);
						}
					} catch (Throwable any) {
						any.printStackTrace(System.out);
						continue;
					}
	
				}
	
			}
		} catch (IOException exp) {
			
		}

		return result;
	}
}
