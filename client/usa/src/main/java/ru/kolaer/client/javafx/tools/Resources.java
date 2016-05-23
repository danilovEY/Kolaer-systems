package ru.kolaer.client.javafx.tools;

import java.io.InputStream;
import java.net.URL;

public interface Resources {
	public static final String VERSION = "1.1";
	
	public static final String PATH_TO_DIR_WITH_PLUGINS = "plugins";
	public static final StringBuilder URL_TO_KOLAER_RESTFUL = new StringBuilder("localhost:8080/ru.kolaer.server.restful");
	
	public static final URL V_MAIN_FRAME = Resources.class.getResource("/viewFX/VMainFrame.fxml");
	public static final URL V_TAB_EXPLORER = Resources.class.getResource("/viewFX/VTabExplorer.fxml");
	
	public static final URL ICON_START_BUTTON = Resources.class.getResource("/css/aerIcon.png");
	public static final InputStream ICON_MAIN_FRAME = Resources.class.getResourceAsStream("/css/favicon.ico");
	
	public static final String L_MENU_FILE = "Файл";
	public static final String L_MENU_HELP = "Помощь";
	
}	