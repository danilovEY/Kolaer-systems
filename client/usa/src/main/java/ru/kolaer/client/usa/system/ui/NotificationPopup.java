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
import java.util.concurrent.TimeUnit;

/**
 * Реализация интерфейса для работы с оповещением.
 *
 * @author danilovey
 * @version 0.1
 */
public class NotificationPopup implements NotificationUS {

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
			final Notifications Notifi = this.addActions(Notifications.create(), actions);
			Notifi.hideAfter(Duration.seconds(15));
			Notifi.position(Pos.BOTTOM_RIGHT);
			if(title != null)
				Notifi.title(title);
			if(text != null) {
				Notifi.text(text);
			} 
			Notifi.showError();
		});
	}

	@Override
	public void showWarningNotify(final String title, final String text) {
		this.showWarningNotify(title, text, Collections.emptyList());
	}
	
	@Override
	public void showWarningNotify(final String title, final String text, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications Notifi = this.addActions(Notifications.create(), actions);
			Notifi.hideAfter(Duration.seconds(10));
			Notifi.position(Pos.BOTTOM_RIGHT);
			if(title != null)
				Notifi.title(title);
			if(text != null) {
				Notifi.text(text);
			} 
			Notifi.showWarning();
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

	private Notifications addActions(Notifications Notifi, List<NotifyAction> actions) {
		Tools.runOnThreadFXAndWain(() -> {
			if(actions != null && actions.size() != 0) {
				final VBox vBox = new VBox();
				
				for(NotifyAction notifyAction : actions) {
					final Button action = new Button(notifyAction.getText());
					action.setMaxWidth(Double.MAX_VALUE);
					action.setOnAction(e -> {
						notifyAction.getConsumer().accept(e);
					});
					vBox.getChildren().add(action);
				}
				Notifi.graphic(vBox);
			}	
		}, 20, TimeUnit.SECONDS);
		return Notifi;
	}

	@Override
	public void showSimpleNotify(final String title, final  String text, final Duration duration, final Pos pos, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications Notifi = this.addActions(Notifications.create(), actions);
			Notifi.hideAfter(duration);	
			Notifi.position(pos);
			if(title != null)
				Notifi.title(title);
			if(text != null) {
				Notifi.text(text);
			} 	
			Notifi.show();
		});
	}

	@Override
	public void showInformationNotify(final String title, final String text, final Duration duration, final Pos pos, final List<NotifyAction> actions) {
		Tools.runOnWithOutThreadFX(() -> {
			final Notifications Notifi = this.addActions(Notifications.create(), actions);
			Notifi.hideAfter(duration);
			Notifi.position(pos);
			if(title != null)
				Notifi.title(title);
			if(text != null) {
				Notifi.text(text);
			}
			Notifi.showInformation();
		});
	}
}