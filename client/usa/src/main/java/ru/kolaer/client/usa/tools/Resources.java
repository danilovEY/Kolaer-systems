package ru.kolaer.client.usa.tools;

import java.io.InputStream;
import java.net.URL;

public interface Resources {
	String CACHE_PATH = "D:\\Документы\\KolaerCache";

	String PRIVATE_SERVER_URL_PARAM = "private-server";
	String PUBLIC_SERVER_URL_PARAM = "public-server";
	String SERVICE_PARAM = "service";
	String TRAY_PARAM = "tray";
	String RAND_DIR_CACHE_PARAM = "rand-dir-cache";

	String VERSION = "4.3.5";
	
	String PATH_TO_DIR_WITH_PLUGINS = "plugins";
	StringBuilder URL_TO_PRIVATE_SERVER = new StringBuilder("localhost:8080/ru.kolaer.server.restful");
	StringBuilder URL_TO_PUBLIC_SERVER = new StringBuilder("localhost:8080/kolaer-web");


	URL ICON_START_BUTTON = Resources.class.getResource("/css/aerIcon.png");
	InputStream ICON_MAIN_FRAME = Resources.class.getResourceAsStream("/css/favicon.ico");
	
	String L_MENU_FILE = "Файл";
	String L_MENU_HELP = "Помощь";
	
}	