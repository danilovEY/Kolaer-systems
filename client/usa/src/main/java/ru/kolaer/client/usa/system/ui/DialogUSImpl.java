package ru.kolaer.client.usa.system.ui;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import org.controlsfx.dialog.LoginDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.system.ui.DialogUS;
import ru.kolaer.api.system.ui.UISystemUS;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.tools.Resources;

import java.util.concurrent.Executors;

/**
 * Реализация диалоговых окон.
 *
 * @author danilovey
 * @version 0.1
 */
public class DialogUSImpl implements DialogUS {
	private final Logger log = LoggerFactory.getLogger(DialogUSImpl.class);

	@Override
	public Dialog<?> createSimpleDialog(final String title, final String text) {
		 final Alert dlg = new Alert(AlertType.NONE);
         dlg.setTitle(title);
         dlg.setContentText(text);
         return dlg;
	}

	@Override
	public Dialog<?> createErrorDialog(final String title, final String text) {
		final Alert dlg = new Alert(AlertType.ERROR);
        dlg.setTitle(title);
        dlg.setContentText(text);
        return dlg;
	}

	@Override
	public Dialog<?> createLoadingDialog(final Service<?> service) {
         final ProgressDialog dlg = new ProgressDialog(service);

         service.start();
         return dlg;
	}

	@Override
	public Dialog<?> createLoadingDialog(final Task<?> task) {
         final ProgressDialog dlg = new ProgressDialog(task);
         return dlg;
	}

	@Override
	public void createAndShowLoginToSystemDialog() {
		final Authentication authentication = UniformSystemEditorKitSingleton.getInstance().getAuthentication();
		final UISystemUS uiSystemUS = UniformSystemEditorKitSingleton.getInstance().getUISystemUS();

		final Dialog loginDialog = this.createLoginDialog();
		final CheckBox checkBoxRemember = new CheckBox("Запомни меня");
		((VBox)loginDialog.getDialogPane().getContent()).getChildren().add(checkBoxRemember);
		loginDialog.showAndWait();
		if(loginDialog.getResult() == null)
			return;
		final String[] logPassArray = loginDialog.getResult().toString().split("=");

		final Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				this.updateTitle("Подключение к серверу");
				this.updateMessage("Проверка доступности сервера...");
				ServerResponse<ServerStatus> responceServerStatus = UniformSystemEditorKitSingleton.getInstance().getUSNetwork()
						.getKolaerWebServer().getServerStatus();
				if (!responceServerStatus.isServerError()
						&& responceServerStatus.getResponse() == ServerStatus.AVAILABLE) { // TODO: !!!
					this.updateMessage("Авторизация...");
					try {
						String login = "";
						String pass = "";
						if (logPassArray.length >= 1)
							login = logPassArray[0];
						if (logPassArray.length >= 2)
							pass = logPassArray[1];

						final UserAndPassJson userAndPass = new UserAndPassJson(login, pass);

						if (authentication.login(userAndPass, checkBoxRemember.isSelected())) {
							uiSystemUS.getNotification().showInformationNotify("Успешная авторизация!",
									"Вы авторизовались как: \"" + authentication
											.getAuthorizedUser().getUsername() + "\"");

							return true;
						} else {
							uiSystemUS.getNotification().showErrorNotify("Ошибка!",
									"Авторизироватся не удалось!");
						}
					} catch (Exception ex) {
						updateMessage("Не удалось авторизоваться!!");
						this.setException(ex);
						Tools.runOnWithOutThreadFX(() ->
								createErrorDialog("Ошибка!", "Неудалось авторизоватся!").show()
						);
					}
				} else {
					log.warn("Сервер {} недоступен!", Resources.URL_TO_PUBLIC_SERVER);
					uiSystemUS.getNotification().showErrorNotify("Ошибка!",
							"Сервер недоступен!");
				}
				updateProgress(100, 100);
				return false;
			}
		};

		Tools.runOnThreadFX(() -> this.createLoadingDialog(task).showAndWait());
		Executors.newSingleThreadExecutor().submit(task);
	}

	@Override
	public Dialog<?> createInfoDialog(String title, String text) {
		final Alert dlg = new Alert(AlertType.INFORMATION);
        dlg.setTitle(title);
        dlg.setContentText(text);
        return dlg;
	}

	@Override
	public Dialog<?> createLoginDialog() {
		final LoginDialog dlg = new LoginDialog(null, null);
		return dlg;
	}
}
