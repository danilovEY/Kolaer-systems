package ru.kolaer.client.usa.system.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.api.tools.Tools;

import java.util.Collections;
import java.util.List;

/**
 * Реализация интерфейса для работы с оповещением.
 *
 * @author danilovey
 * @version 0.1
 */
public class NotificationPopupControlFx implements NotificationUS {

	@Override
	public void showSimpleNotify(final String title, final String text) {
		this.showSimpleNotify(title, text, Duration.seconds(5));
	}

	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration) {
		this.showSimpleNotify(title, text, duration, Collections.emptyList());
	}
	
	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration, final List<NotifyAction> actions) {
		this.showSimpleNotify(title, text, duration, Pos.BOTTOM_RIGHT, actions);
	}

	@Override
	public void showErrorNotify(final String title, final String text) {
		this.showErrorNotify(title, text, Collections.emptyList());
	}
	
	@Override
	public void showErrorNotify(final String title, final String text, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(Duration.seconds(15));
			notify.position(Pos.BOTTOM_RIGHT);
			if(title != null)
				notify.title(title);
			if(text != null) {
				notify.text(text);
			}
			notify.showError();
		});
	}

	@Override
	public void showWarningNotify(final String title, final String text) {
		this.showWarningNotify(title, text, Collections.emptyList());
	}
	
	@Override
	public void showWarningNotify(final String title, final String text, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(Duration.seconds(10));
			notify.position(Pos.BOTTOM_RIGHT);
			if(title != null)
				notify.title(title);
			if(text != null) {
				notify.text(text);
			} 
			notify.showWarning();
		});
	}

	@Override
	public void showInformationNotify(final String title, final String text) {
		this.showInformationNotify(title, text, Duration.seconds(5));
	}

	@Override
	public void showInformationNotify(final String title, final String text, final Duration duration) {
		this.showInformationNotify(title, text, duration, Collections.emptyList());
	}
	
	@Override
	public void showInformationNotify(final String title, final String text, final Duration duration, final List<NotifyAction> actions) {
		this.showInformationNotify(title, text, duration, Pos.BOTTOM_RIGHT, actions);
	}

	@Override
	public void showErrorNotify(ServerExceptionMessage exceptionMessage) {
		showErrorNotify("Ошибка", exceptionMessage.getMessage());
	}

	@Override
	public void showErrorNotify(Exception ex) {
		showErrorNotify("Ошибка", ex.getMessage());
	}

	private Notifications addActions(Notifications notify, List<NotifyAction> actions) {
			if(actions != null && actions.size() != 0) {
				final VBox vBox = new VBox();
				
				for(NotifyAction notifyAction : actions) {
					final Button action = new Button(notifyAction.getText());
					action.setMaxWidth(Double.MAX_VALUE);
					action.setOnAction(notifyAction.getConsumer()::accept);
					vBox.getChildren().add(action);
				}
				notify.graphic(vBox);
			}	
		return notify;
	}

	@Override
	public void showSimpleNotify(final String title, final  String text, final Duration duration, final Pos pos, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(duration);
			notify.position(pos);
			notify.hideCloseButton();
			if(title != null)
				notify.title(title);
			if(text != null) {
				notify.text(text);
			}
			notify.show();
		});
	}

	@Override
	public void showInformationNotify(final String title, final String text, final Duration duration, final Pos pos, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(duration);
			notify.position(pos);
			if(title != null)
				notify.title(title);
			if(text != null) {
				notify.text(text);
			}
			notify.showInformation();
		});
	}
}