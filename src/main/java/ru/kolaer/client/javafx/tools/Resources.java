package ru.kolaer.client.javafx.tools;

import java.net.URL;

public interface Resources {
	public static final String VERSION = "0.0.1";
	
	public static final String PATH_TO_DIR_WITH_PLUGINS = "plugins";
	
	public static final URL V_LABEL = Resources.class.getResource("/viewFX/VLabel.fxml");
	public static final URL V_MAIN_FRAME = Resources.class.getResource("/viewFX/VMainFrame.fxml");
	public static final URL V_EXPLORER = Resources.class.getResource("/viewFX/VExplorer.fxml");
	public static final URL V_APPLICATION_ON_TASK_PANE = Resources.class.getResource("/viewFX/VApplicationOnTaskPane.fxml");
	
	public static final String WINDOW_CSS = Resources.class.getResource("/css/window.css").toExternalForm();
	
	
	public static final String L_MENU_FILE = "Файл";
	public static final String L_MENU_HELP = "Помощь";
	
}	