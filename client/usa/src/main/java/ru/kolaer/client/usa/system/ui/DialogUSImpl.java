package ru.kolaer.client.usa.system.ui;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.dialog.LoginDialog;
import org.controlsfx.dialog.ProgressDialog;
import ru.kolaer.client.core.system.Authentication;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.core.system.network.ServerStatus;
import ru.kolaer.client.core.system.network.kolaerweb.KolaerWebServer;
import ru.kolaer.client.core.system.ui.DialogUS;
import ru.kolaer.client.core.system.ui.UISystemUS;
import ru.kolaer.client.core.tools.Tools;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.UserAndPassJson;

import java.util.concurrent.CompletableFuture;

/**
 * Реализация диалоговых окон.
 *
 * @author danilovey
 * @version 0.1
 */
@Slf4j
public class DialogUSImpl implements DialogUS {

	@Override
	public Dialog<?> createSimpleDialog(final String title, final String text) {
		 final Alert dlg = new Alert(AlertType.NONE);
		 dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
         dlg.setTitle(title);
         dlg.setContentText(text);
         return dlg;
	}

	@Override
	public Dialog<?> createErrorDialog(final String title, final String text) {
		final Alert dlg = new Alert(AlertType.ERROR);
		dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
        dlg.setTitle(title);
        dlg.setContentText(text);
        return dlg;
	}

	@Override
	public Dialog<?> createWarningDialog(String title, String text) {
		Alert dlg = new Alert(AlertType.WARNING);
		dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
		dlg.setTitle(title);
		dlg.setContentText(text);
		return dlg;
	}

	@Override
	public Dialog<?> createLoadingDialog(final Service<?> service) {
         ProgressDialog dlg = new ProgressDialog(service);
         dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
         service.start();
         return dlg;
	}

	@Override
	public Dialog<?> createLoadingDialog(final Task<?> task) {
         ProgressDialog dlg = new ProgressDialog(task);
         dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
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
				KolaerWebServer kolaerWebServer = UniformSystemEditorKitSingleton.getInstance()
						.getUSNetwork()
						.getKolaerWebServer();
				ServerResponse<ServerStatus> responceServerStatus = kolaerWebServer
						.getServerStatus();
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
						log.error("Не удалось авторизоваться!", ex);
						updateMessage("Не удалось авторизоваться!");
						this.setException(ex);
						Tools.runOnWithOutThreadFX(() ->
								createErrorDialog("Ошибка!", "Неудалось авторизоватся!").show()
						);
					}
				} else {
					log.warn("Сервер {} недоступен!", kolaerWebServer.getUrl());
					uiSystemUS.getNotification().showErrorNotify("Ошибка!",
							"Сервер недоступен!");
				}
				updateProgress(100, 100);
				return false;
			}
		};


		Tools.runOnWithOutThreadFX(() -> {
			this.createLoadingDialog(task).show();
		});

		CompletableFuture.runAsync(task);
	}

	@Override
	public Dialog<?> createInfoDialog(String title, String text) {
		final Alert dlg = new Alert(AlertType.INFORMATION);
		dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
        dlg.setTitle(title);
        dlg.setContentText(text);
        return dlg;
	}

	@Override
	public Dialog<?> createLoginDialog() {
		LoginDialog dlg = new LoginDialog(null, null);
		dlg.initOwner(UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getMainStage());
		return dlg;
	}
}
