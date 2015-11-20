package ru.kolaer.asmc.tools;

import java.net.URL;

/**
 * Ресурсы и константы.
 * @author Danilov E.Y.
 *
 */
public interface Resources
{
	/*==========View Resources=============*/
	public static final URL V_MAIN_FRAME = Resources.class.getResource("/ru/kolaer/asmc/ui/javafx/view/VMainFrame.fxml");
	public static final URL V_GROUP_LABELS = Resources.class.getResource("/ru/kolaer/asmc/ui/javafx/view/VGroupLabels.fxml");
	public static final URL V_LABEL = Resources.class.getResource("/ru/kolaer/asmc/ui/javafx/view/VLabel.fxml");
	public static final URL V_AUTHENTICATION = Resources.class.getResource("/ru/kolaer/asmc/ui/javafx/view/VAuthentication.fxml");
	public static final URL V_ADDING_GROUP_LABELS = Resources.class.getResource("/ru/kolaer/asmc/ui/javafx/view/VAddingGroupLabels.fxml");
	
	
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
	
	
	public static final String DIALOG_LOADING_COMPONENTS = "Загрузка компонентов";
	
	public static final String MAIN_FRAME_TITLE = "АСУП - АЭР";
	public static final String LOGIN_FRAME_TITLE = "Получить root доступ";
	
	public static final String LOGIN_LABEL = "Логин";
	public static final String PASS_LABEL = "Пароль";
	
	public static final String LOGIN_BUTTON = "Войти";
	public static final String LOGUOT_BUTTON = "Отмена";
	
	public static final String MENU_ITEM_ADD_GROUP = "Добавить группу";
	
	public static final String MENU_BAR_FILE = "Файл";
	public static final String MENU_BAR_HELP = "Справка";
	public static final String MENU_BAR_ITEM_SETTING = "Настройки";
	public static final String MENU_BAR_ITEM_EXIT = "Выход";
	public static final String MENU_BAR_ITEM_ABOUT = "О программе";	
}
