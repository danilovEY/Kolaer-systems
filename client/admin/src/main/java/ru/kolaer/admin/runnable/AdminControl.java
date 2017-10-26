package ru.kolaer.admin.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import ru.kolaer.admin.service.PprService;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class AdminControl implements UniformSystemPlugin {
	private BorderPane mainPane;
	private UniformSystemEditorKit editorKit;
	private List<Service> serviceList;

	@Override
	public Parent getContent() {
		return mainPane;
	}

	@Override
	public void initialization(final UniformSystemEditorKit editorKit) throws Exception {
		this.editorKit = editorKit;
		this.serviceList = Collections.singletonList(new PprService(editorKit));
	}

	private void sendMessage(String message) {
		final NotifyMessageDto notifyMessage = new NotifyMessageDto();
		notifyMessage.setMessage(message);

		editorKit.getUSNetwork().getKolaerWebServer().getApplicationDataBase()
				.getNotifyMessageTable().addNotifyMessage(notifyMessage);

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

	@Override
	public void initView(Consumer<UniformSystemPlugin> viewVisit) {
		TextField message = new TextField();
		Button sent = new Button("Отправить!");
		sent.setOnAction(e -> {
			if(!editorKit.getAuthentication().isAuthentication()) {
				final Dialog loginDialog = editorKit.getUISystemUS().getDialog().createLoginDialog();
				loginDialog.showAndWait();
				if(loginDialog.getResult() == null)
					return;
				final String[] logPassArray = loginDialog.getResult().toString().split("=");

				/*Task<Object> worker = new Task<Object>() {
					@Override
					protected Object call() throws Exception {
						updateTitle("Подключение к серверу");
						updateMessage("Проверка доступности сервера...");
						if(editorKit.getUSNetwork().getKolaerWebServer().getServerStatus().getResponse() == ServerStatus.AVAILABLE) {
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
					this.sendMessage(message.getText());
				});*/
			} else {
				this.sendMessage(message.getText());
			}

		});
		this.mainPane = new BorderPane(new HBox(message, sent));
		viewVisit.accept(this);
	}
}
