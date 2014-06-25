package ru.kolaer.GUI;

import java.net.URL;

/**
 * Ресурсы приложения.
 * @author Danilov E.Y.
 *
 */
public interface Resources
{
	/**Стандарткая иконка для ярлыка.*/
	public static URL AER_ICON = Resources.class.getResource("/aerIcon.png");
	/**Иконка для приложения.*/
	public static URL AER_LOGO = Resources.class.getResource("/aerLogo.png");
	/**Путь к простому браузеру.*/
	public static String WEB_BROWSER = "Utilites/WebBrowser.exe";
}
