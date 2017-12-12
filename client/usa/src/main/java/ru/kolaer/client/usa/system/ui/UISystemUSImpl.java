package ru.kolaer.client.usa.system.ui;

import javafx.stage.Stage;
import ru.kolaer.api.system.ui.*;

/**
 * Реализация системных (приложения) объектов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UISystemUSImpl implements UISystemUS {
	private NotificationUS notification;
	private NotificationUS popupNotification;
	private DialogUS dialog = new DialogUSImpl();
	private StatusBarUS statusBar;
	private StaticUS staticUS;
	private MenuBarUS menuBarUS;
	private Stage mainStage;

	public UISystemUSImpl(final StatusBarUS statusBar) {
		this.statusBar = statusBar;
		this.popupNotification = new NotificationPopup();
	}
	
	public UISystemUSImpl() {
		this(new StatusBarUSAdapter());
	}

	@Override
	public NotificationUS getNotification() {
		return this.notification;
	}

	@Override
	public NotificationUS getPopupNotification() {
		return this.popupNotification;
	}

	@Override
	public DialogUS getDialog() {
		return this.dialog;
	}

	@Override
	public StatusBarUS getStatusBar() {	
		return this.statusBar;
	}

	@Override
	public StaticUS getStatic() {
		return this.staticUS;
	}

	@Override
	public MenuBarUS getMenuBar() {
		return this.menuBarUS;
	}

	@Override
	public Stage getMainStage() {
		return mainStage;
	}

	public void setNotification(final NotificationUS notificationUS) {
		this.notification = notificationUS;
	}

	public void setMenuBarUS(MenuBarUS menuBarUS) {this.menuBarUS = menuBarUS;}

	public void setDialog(final DialogUS dialogUS) {
		this.dialog = dialogUS;
	}

	public void setStatusBar(final StatusBarUS statusBarUS) {
		this.statusBar = statusBarUS;
	}

	public void setStaticUS(StaticUS staticUS) {
		this.staticUS = staticUS;
	}

	public void setMainStage(Stage mainStage) {
		this.mainStage = mainStage;
	}
}
