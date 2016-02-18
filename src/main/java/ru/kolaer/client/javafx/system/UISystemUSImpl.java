package ru.kolaer.client.javafx.system;

import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

/**
 * Реализация системных (приложения) объектов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UISystemUSImpl implements UISystemUS {
	private NotificationUS notification = new NotificationUSImpl();
	private DialogUS dialog = new DialogUSImpl();
	private StatusBarUS statusBar;
	private VMExplorer explorer;
	
	public UISystemUSImpl(final VMExplorer explorer, final StatusBarUS statusBar) {
		this.statusBar = statusBar;
		this.explorer = explorer;
	}
	
	public UISystemUSImpl(final StatusBarUS statusBar) {
		this(null, statusBar);
	}
	
	public UISystemUSImpl(final VMExplorer explorer) {
		this(explorer, new StatusBarUSAdapter());
	}
	
	public UISystemUSImpl() {
		this(null, new StatusBarUSAdapter());
	}

	@Override
	public NotificationUS getNotification() {
		return this.notification;
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
	public VMExplorer getExplorer() {
		return this.explorer;
	}

	@Override
	public void setNotification(final NotificationUS notificationUS) {
		this.notification = notificationUS;
	}

	@Override
	public void setDialog(final DialogUS dialogUS) {
		this.dialog = dialogUS;
	}

	@Override
	public void setStatusBar(final StatusBarUS statusBarUS) {
		this.statusBar = statusBarUS;
	}

	@Override
	public void setExplorer(final VMExplorer vmExplorer) {
		this.explorer = vmExplorer;
	}
}
