package ru.kolaer.client.javafx.system.ui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import ru.kolaer.api.system.ui.NotifiAction;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.tools.Tools;

import java.util.concurrent.TimeUnit;

/**
 * Реализация интерфейса для работы с оповещением.
 *
 * @author danilovey
 * @version 0.1
 */
public class NotificationPopup implements NotificationUS {

	@Override
	public void showParentNotifi(Parent pane) {

	}

	@Override
	public void removeParentNotifi(Parent content) {

	}

	@Override
	public void showSimpleNotifi(final String title, final String text) {
		this.showSimpleNotifi(title, text, Duration.seconds(5));
	}

	@Override
	public void showSimpleNotifi(final String title, final String text, Duration duration) {
		this.showSimpleNotifi(title, text, duration, new NotifiAction[0]);
	}
	
	@Override
	public void showSimpleNotifi(final String title, final String text, Duration duration, final NotifiAction... actions) {
		this.showSimpleNotifi(title, text, duration, Pos.BOTTOM_RIGHT, actions);
	}

	@Override
	public void showErrorNotifi(final String title, final String text) {	
		this.showErrorNotifi(title, text, new NotifiAction[0]);
	}
	
	@Override
	public void showErrorNotifi(final String title, final String text, final NotifiAction... actions) {	
		Tools.runOnThreadFX(() -> {
			final Notifications Notifi = this.addActions(Notifications.create(), actions);
			Notifi.hideAfter(Duration.seconds(15));
			Notifi.position(Pos.BOTTOM_LEFT);
			if(title != null)
				Notifi.title(title);
			if(text != null) {
				Notifi.text(text);
			} 
			Notifi.showError();
		});
	}

	@Override
	public void showWarningNotifi(final String title, final String text) {		
		this.showWarningNotifi(title, text, new NotifiAction[0]);
	}
	
	@Override
	public void showWarningNotifi(final String title, final String text, final NotifiAction... actions) {		
		Tools.runOnThreadFX(() -> {
			final Notifications Notifi = this.addActions(Notifications.create(), actions);
			Notifi.hideAfter(Duration.seconds(10));
			Notifi.position(Pos.BOTTOM_LEFT);
			if(title != null)
				Notifi.title(title);
			if(text != null) {
				Notifi.text(text);
			} 
			Notifi.showWarning();
		});
	}

	@Override
	public void showInformationNotifi(final String title, final String text) {
		this.showInformationNotifi(title, text, Duration.seconds(5));
	}

	@Override
	public void showInformationNotifi(final String title, final String text, final Duration duration) {
		this.showInformationNotifi(title, text, duration, new NotifiAction[0]);
	}
	
	@Override
	public void showInformationNotifi(final String title, final String text, final Duration duration, final NotifiAction... actions) {
		this.showInformationNotifi(title, text, duration, Pos.BOTTOM_RIGHT, actions);
	}

	@Override
	public void showInformationNotifiAdmin(String title, String text, NotifiAction... actions) {

	}

	@Override
	public void showWarningNotifiAdmin(String title, String text, NotifiAction... actions) {

	}

	private Notifications addActions(final Notifications Notifi, final NotifiAction... actions) {
		Tools.runOnThreadFXAndWain(() -> {
			if(actions != null && actions.length != 0) {
				final Action[] actionsObj = new Action[actions.length];	
				final VBox vBox = new VBox();
				
				for(int i = 0; i < actions.length; i++) {
					final NotifiAction NotifiAction = actions[i];
					actionsObj[i] = new Action(NotifiAction.getText(), NotifiAction.getConsumer());
					final Button action = new Button(NotifiAction.getText());
					action.setMaxWidth(Double.MAX_VALUE);
					action.setOnAction(e -> {
						NotifiAction.getConsumer().accept(e);
					});
					vBox.getChildren().add(action);
				}
				Notifi.graphic(vBox);
			}	
		}, 20, TimeUnit.SECONDS);
		return Notifi;
	}

	@Override
	public void showSimpleNotifi(final String title, final  String text, final Duration duration, final Pos pos, final NotifiAction... actions) {
		Tools.runOnThreadFX(() -> {
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
	public void showInformationNotifi(final String title, final String text, final Duration duration, final Pos pos, final NotifiAction... actions) {
		Tools.runOnThreadFX(() -> {
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