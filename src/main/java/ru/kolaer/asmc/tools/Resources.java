package ru.kolaer.asmc.tools;

/**
 * Ресурсы и константы.
 * @author Danilov E.Y.
 *
 */
public interface Resources
{
	/*==========View Resources=============*/
	public static final String V_MAIN_FRAME = "file:view/VMainFrame.fxml";
	public static final String V_GROUP_LABELS = "file:view/VGroupLabels.fxml";
	public static final String V_LABEL = "file:view/VLabel.fxml";
	public static final String V_AUTHENTICATION = "file:view/VAuthentication.fxml";
	public static final String V_ADDING_GROUP_LABELS = "file:view/VAddingGroupLabels.fxml";
	public static final String V_ADDING_LABEL = "file:view/VAddingLabel.fxml";
	public static final String V_WEB_BROWSER = "file:view/VWebBrowser.fxml";
	public static final String V_SETTING = "file:view/VSetting.fxml";
	
	
	/**Стандарткая иконка для ярлыка.*/
	public static final String AER_ICON = "resources/aerIcon.png";
	/**Иконка для приложения.*/
	public static final String AER_LOGO = "resources/aerLogo.png";
	/**Иконка для приложения.*/
	public static final String ABOUT_INFO_IMAGE = "resources/aboutInfo.jpg";
	/**Фон.*/
	public static final String BACKGROUND_IMAGE = "resources/geometric-background.png";
	/**Версия*/
	public static final String VERSION = "3.0";	
	
	public static final String DIALOG_LOADING_COMPONENTS = "Загрузка компонентов";
	
	public static final String MAIN_FRAME_TITLE = "АСУП - АЭР" + " v" + VERSION;
	public static final String SETTING_LABEL_FRAME_TITLE = "Настройки";
	public static final String LOGIN_FRAME_TITLE = "Получить root доступ";
	public static final String ADDING_GROUP_FRAME_TITLE = "Добавить группу";
	public static final String EDING_GROUP_FRAME_TITLE = "Редактировать группу";
	public static final String ADDING_LABEL_FRAME_TITLE = "Добавить ярлык";
	public static final String EDDING_LABEL_FRAME_TITLE = "Редактировать ярлык";
	
	public static final String LOGIN_LABEL = "Логин";
	public static final String PASS_LABEL = "Пароль";
	
	public static final String LOGIN_BUTTON = "Войти";
	public static final String LOGUOT_BUTTON = "Отмена";
	
	public static final String MENU_ITEM_ADD_GROUP = "Добавить группу";
	public static final String MENU_ITEM_EDIT_GROUP = "Редактировать группу";
	public static final String MENU_ITEM_DELETE_GROUP = "Удалить группу";
	
	public static final String MENU_ITEM_ADD_LABEL = "Добавить ярлык";
	public static final String MENU_ITEM_EDIT_LABEL = "Редактировать ярлык";
	public static final String MENU_ITEM_DELETE_LABEL = "Удалить ярлык";
	
	public static final String MENU_ITEM_BROWSER_COPY = "Копировать";
	public static final String MENU_ITEM_BROWSER_PLASE = "Вставить";
}
