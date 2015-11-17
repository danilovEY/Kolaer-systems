package ru.kolaer.asmc.tools;

import java.net.URL;

/**
 * Ресурсы приложения.
 * @author Danilov E.Y.
 *
 */
public interface Resources
{
	/*==========View Resources=============*/
	public static final URL V_MAIN_FRAME = Resources.class.getResource("/ru/kolaer/asmc/ui/javafx/view/VMainFrame.fxml");
	
	
	/**Стандарткая иконка для ярлыка.*/
	public static final URL AER_ICON = Resources.class.getResource("/aerIcon.png");
	/**Иконка для приложения.*/
	public static final URL AER_LOGO = Resources.class.getResource("/aerLogo.png");
	/**Иконка для приложения.*/
	public static final URL ABOUT_INFO_IMAGE = Resources.class.getResource("/aboutInfo.jpg");
	/**Путь к простому браузеру.*/
	public static final String WEB_BROWSER = "Utilites/WebBrowser.exe";
	/**Версия*/
	public static final String VERSION = "0.7.7.3";
}
