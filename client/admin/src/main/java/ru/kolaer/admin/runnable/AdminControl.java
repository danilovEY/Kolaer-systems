package ru.kolaer.admin.runnable;

import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import ru.kolaer.admin.service.PprService;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageBase;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.tools.Tools;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminControl implements UniformSystemPlugin {
	private BorderPane mainPane;
	private UniformSystemEditorKit editorKit;
	private List<Service> serviceList;

	@Override
	public void setContent(Parent content) {
		
	}

	@Override
	public Parent getContent() {
		return mainPane;
	}

	@Override
	public void initialization(final UniformSystemEditorKit editorKit) throws Exception {
		this.editorKit = editorKit;
		this.serviceList = Arrays.asList(new PprService(editorKit));
		Tools.runOnThreadFX(() -> {
			final TextField message = new TextField();
			final Button sent = new Button("Отправить!");
			sent.setOnAction(e -> {
				if(!editorKit.getAuthentication().isAuthentication()) {
					final Dialog loginDialog = editorKit.getUISystemUS().getDialog().createLoginDialog();
					loginDialog.showAndWait();
					if(loginDialog.getResult() == null)
						return;
					final String[] logPassArray = loginDialog.getResult().toString().split("=");

					Task<Object> worker = new Task<Object>() {
						@Override
						protected Object call() throws Exception {
							updateTitle("Подключение к серверу");
							updateMessage("Проверка доступности сервера...");
							if(editorKit.getUSNetwork().getKolaerWebServer().getServerStatus() == ServerStatus.AVAILABLE) {
								updateMessage("Авторизация...");
								try {
									String login = "";
									String pass = "";
									if(logPassArray.length >= 1)
										login = logPassArray[0];
									if(logPassArray.length >= 2)
										pass =  logPassArray[1];
									editorKit.getAuthentication().login(new UserAndPassJson(login, pass));
								} catch (ServerException ex) {
									updateMessage("Не удалось авторизоваться!!");
									this.setException(ex);
									Tools.runOnThreadFX(() ->{
										editorKit.getUISystemUS().getDialog().createErrorDialog("Ошибка!", "Неудалось авторизоватся!").show();
									});
								}
							}
							updateProgress(100,100);
							return null;
						}
					};

					Tools.runOnThreadFX(() -> {
						editorKit.getUISystemUS().getDialog().createLoadingDialog(worker).showAndWait();
					});

					ExecutorService authThread = Executors.newSingleThreadExecutor();

					CompletableFuture.runAsync(worker, authThread).thenAccept(result -> {
						//this.sendMessage(message.getText());
					});
				} else {
					//this.sendMessage(message.getText());
				}



			});
			this.mainPane = new BorderPane(new HBox(message, sent));
		});
	}

	private void sendMessage(String message) {
		final NotifyMessage notifyMessage = new NotifyMessageBase();
		notifyMessage.setMessage(message);
		try {
			editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getNotifyMessageTable().addNotifyMessage(notifyMessage);
		}catch (ServerException ex) {
			editorKit.getUISystemUS().getNotification().showErrorNotifi("Ошибка", "Не удалось отправить!");
		}
	}

	@Override
	public URL getIcon() {
		return null;
	}

	@Override
	public Collection<Service> getServices() {
		return this.serviceList;
	}

	@Override
	public void start() throws Exception {

	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void updatePluginObjects(String key, Object object) {

	}

}
