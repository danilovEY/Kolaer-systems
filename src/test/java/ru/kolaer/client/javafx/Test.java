package ru.kolaer.client.javafx;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import ru.kolaer.client.javajx.plugins.ApplicationPlugin;
import ru.kolaer.client.javajx.plugins.DesktopLabel;

public class Test {

	public static void main(String[] args) throws IOException {
		JarFile jarFile = new JarFile("D:/test.jar");
		Enumeration e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + "D:/test.jar!/") };
		ClassLoader cl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

		//final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		//provider.setResourceLoader(new PathMatchingResourcePatternResolver(cl));
		// add include filters which matches all the classes (or use your own)
		// provider.addIncludeFilter(new
		// AnnotationTypeFilter(DesktopLabel.class));
		// provider.addIncludeFilter(new
		// AnnotationTypeFilter(ApplicationPlugin.class));

		while (e.hasMoreElements()) {
			JarEntry je = (JarEntry) e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			try {
				Class c = cl.loadClass(className);
				System.out.println(c.getName());
				for(Annotation a : c.getAnnotations()) {
					System.out.println(a.toString());
				}
			} catch (ClassNotFoundException e1) {

			} finally {
				continue;
			}

		}

		// get matching classes defined in the package
		/*final Set<BeanDefinition> classes = provider.findCandidateComponents("");

		// this is how you can load the class type from BeanDefinition instance
		for (BeanDefinition bean : classes) {
			System.out.println(bean.getBeanClassName());
		}*/

	}

}
