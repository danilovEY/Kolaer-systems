package ru.kolaer.asmc.tools;

import java.net.URL;

/**
 * Ресурсы приложения.
 * @author Danilov E.Y.
 *
 */
public abstract class Resources
{
	/**Стандарткая иконка для ярлыка.*/
	public static URL AER_ICON = Resources.class.getResource("/aerIcon.png");
	/**Иконка для приложения.*/
	public static URL AER_LOGO = Resources.class.getResource("/aerLogo.png");
	/**Иконка для приложения.*/
	public static URL ABOUT_INFO_IMAGE = Resources.class.getResource("/aboutInfo.jpg");
	/**Путь к простому браузеру.*/
	public static String WEB_BROWSER = "Utilites/WebBrowser.exe";
	/**Версия*/
	public static String VERSION = "0.7.7.3";
}
