package ru.kolaer.ru.client.javafx.runnable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.ILabel;
import ru.kolaer.client.javafx.plugins.PluginLoader;

public class Test {

	public static void main(String[] args) {
		
		new PluginLoader("D:/").scanPlugins();
		
		/*try {
			URL jarURL = new File("D:/test.jar").toURI().toURL();
			URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
			Class clsAA = classLoader.loadClass("ru.kolaer.test.HelloWorldApplication");
			IKolaerPlugin app = (IKolaerPlugin) clsAA.newInstance();
			
			ILabel label = app.getLabel();
			System.out.println(label.getName());
			
			IApplication content = app.getApplication();
			System.out.println(content.getName());
			//System.out.println(clsAA.getName());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
		
		/*JarFile jarFile;
		URLClassLoader cl = null;
		Enumeration e = null;
		try {
			jarFile = new JarFile("D:/test.jar");
			e = jarFile.entries();
	
			URL[] urls = { new URL("jar:file:" + "D:/test.jar!/") };
			cl = new URLClassLoader(urls);
		}catch (IOException e3) {
				e3.printStackTrace();
		}
		
		while (e.hasMoreElements()) {
			JarEntry je = (JarEntry) e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			try {
				System.out.println("className: "+className);
				Class c = cl.loadClass(className);
				System.out.println("c.getName(): "+c.getName());
				if(c.getAnnotation(DesktopLabel.class) != null){
					System.out.println("AA");
				}
			} catch (ClassNotFoundException e2) {
				System.out.println("Error!");
				continue;
			}

		}*/

	}

}
