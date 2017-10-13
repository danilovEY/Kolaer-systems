package ru.kolaer.client.usa.tools;

import java.io.InputStream;
import java.net.URL;

public interface Resources {
	String VERSION = "1.2";
	
	String PATH_TO_DIR_WITH_PLUGINS = "plugins";
	StringBuilder URL_TO_KOLAER_RESTFUL = new StringBuilder("localhost:8080/ru.kolaer.server.restful");
	StringBuilder URL_TO_KOLAER_WEB = new StringBuilder("localhost:8080/kolaer-web");

	URL V_MAIN_FRAME = Resources.class.getResource("/viewFX/VMainFrame.fxml");
	URL V_TAB_EXPLORER = Resources.class.getResource("/viewFX/VTabExplorer.fxml");
	
	URL ICON_START_BUTTON = Resources.class.getResource("/css/aerIcon.png");
	InputStream ICON_MAIN_FRAME = Resources.class.getResourceAsStream("/css/favicon.ico");
	
	String L_MENU_FILE = "Файл";
	String L_MENU_HELP = "Помощь";
	
}	