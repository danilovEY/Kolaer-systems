package ru.kolaer.client.javafx.system;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import ru.kolaer.api.system.NotificationUS;
import ru.kolaer.api.system.NotifyAction;
import ru.kolaer.api.tools.Tools;

/**
 * Реализация интерфейса для работы с оповещением.
 *
 * @author danilovey
 * @version 0.1
 */
public class NotificationUSImpl implements NotificationUS {

	@Override
	public void showSimpleNotify(final String title, final String text) {
		this.showSimpleNotify(title, text, Duration.seconds(5));
	}

	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration) {
		this.showSimpleNotify(title, text, duration, new NotifyAction[0]);
	}
	
	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration, final NotifyAction... actions) {
		this.showSimpleNotify(title, text, duration, Pos.BOTTOM_RIGHT, actions);
	}

	@Override
	public void showErrorNotify(final String title, final String text) {	
		this.showErrorNotify(title, text, new NotifyAction[0]);
	}
	
	@Override
	public void showErrorNotify(final String title, final String text, final NotifyAction... actions) {	
		Tools.runOnThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(Duration.seconds(15));
			notify.position(Pos.BOTTOM_CENTER);
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
		this.showWarningNotify(title, text, new NotifyAction[0]);
	}
	
	@Override
	public void showWarningNotify(final String title, final String text, final NotifyAction... actions) {		
		Tools.runOnThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(Duration.seconds(10));
			notify.position(Pos.BOTTOM_CENTER);
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
		this.showInformationNotify(title, text, duration, new NotifyAction[0]);
	}
	
	@Override
	public void showInformationNotify(final String title, final String text, final Duration duration, final NotifyAction... actions) {
		this.showInformationNotify(title, text, duration, Pos.BOTTOM_RIGHT, actions);
	}
	
	private synchronized Notifications addActions(final Notifications notify, final NotifyAction... actions) {
		if(actions != null && actions.length != 0) {
			final Action[] actionsObj = new Action[actions.length];	
			final VBox vBox = new VBox();
			
			for(int i = 0; i < actions.length; i++) {
				final NotifyAction notifyAction = actions[i];
				actionsObj[i] = new Action(notifyAction.getText(), notifyAction.getConsumer());
				final Button action = new Button(notifyAction.getText());
				action.setMaxWidth(Double.MAX_VALUE);
				action.setOnAction(e -> {
					notifyAction.getConsumer().accept(e);
				});
				vBox.getChildren().add(action);
			}
			notify.graphic(vBox);
		}	
		return notify;
	}

	@Override
	public void showSimpleNotify(final String title, final  String text, final Duration duration, final Pos pos, final NotifyAction... actions) {
		Tools.runOnThreadFX(() -> {
			final Notifications notify = this.addActions(Notifications.create(), actions);
			notify.hideAfter(duration);	
			notify.position(pos);
			if(title != null)
				notify.title(title);
			if(text != null) {
				notify.text(text);
			} 	
			notify.show();
		});
	}

	@Override
	public void showInformationNotify(final String title, final String text, final Duration duration, final Pos pos, final NotifyAction... actions) {
		Tools.runOnThreadFX(() -> {
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