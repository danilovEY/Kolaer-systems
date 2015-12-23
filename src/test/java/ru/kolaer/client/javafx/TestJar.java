package ru.kolaer.client.javafx;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

public class TestJar {

	public static void main(String[] args) {
		File file = new File("D:\\workspace\\ru.kolaer.client.javafx\\plugins\\helloworld4.jar");
		try(final JarFile jarFileRead = new JarFile(file)) {
			final Enumeration<?> e = jarFileRead.entries();
			//jarFileRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
