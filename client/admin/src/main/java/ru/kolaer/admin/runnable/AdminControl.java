package ru.kolaer.admin.runnable;

import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminControl implements UniformSystemPlugin {
	private BorderPane mainPane;
	private UniformSystemEditorKit editorKit;
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
		Tools.runOnThreadFX(() -> {
			final TextField message = new TextField();
			final Button sent = new Button("Отправить!");
			sent.setOnAction(e -> {
				if(!editorKit.getAuthentication().isAuthentication()) {
					final Dialog loginDialog = editorKit.getUISystemUS().getDialog().createLoginDialog();
					loginDialog.showAndWait();
					if(loginDialog.getResult() == null)
						return;
					String[] logPassArray = loginDialog.getResult().toString().split("=");
					Task<Object> worker = new Task<Object>() {
						@Override
						protected Object call() throws Exception {
							updateTitle("Подключение к серверу");
							updateMessage("Проверка доступности сервера...");
							if(editorKit.getUSNetwork().getKolaerWebServer().getServerStatus() == ServerStatus.AVAILABLE) {
								updateMessage("Авторизация...");
								try {
									editorKit.getAuthentication().login(new UserAndPassJson(logPassArray[0], logPassArray[1]));
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
					});
				} else {
					this.sendMessage(message.getText());
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
		return null;
	}

	@Override
	public void start() throws Exception {
		/*Tools.runOnThreadFX(() -> {
			final MenuItem logIn = new MenuItem("Войти");
			final Menu fileMenu = new Menu("Файл");
			fileMenu.getItems().addAll(logIn);
			
			final MenuBar menuBar = new MenuBar(fileMenu);
			
			logIn.setOnAction(e -> {
				final Dialog<?> login = this.editorKid.getUISystemUS().getDialog().createLoginDialog();
				login.showAndWait();
				final String[] logAndPass = login.getResult().toString().split("=");
				if(logAndPass.length == 2 && logAndPass[0].equals("root") && logAndPass[1].equals(pass)) {			
					final BidModelManager bidModelManager = new BidModelManager();
					
					final ExecutorService executorService = Executors.newSingleThreadExecutor();
					CompletableFuture.runAsync(() -> {
						final boolean isConnect = bidModelManager.connectToDB();
						
						if(!isConnect) {
							this.editorKid.getUISystemUS().getDialog().createErrorDialog("Ошибка!", "Не удалось подключиться к БД!").show();
						} else {
							Tools.runOnThreadFX(() -> {
								logIn.setDisable(true);
								final TabPane tabPane = new TabPane();
								this.mainPane.setCenter(tabPane);
								tabPane.getTabs().add(new PBidTable(bidModelManager).getVTabContent().getTab());
							});
						}
					}, executorService).exceptionally(t -> {
						return null;
					});
					
					final MenuItem exportToXls = new MenuItem("Экспортировать в Excel...");
					exportToXls.setDisable(true);

					fileMenu.getItems().add(exportToXls);
				} else {
					this.editorKid.getUISystemUS().getDialog().createErrorDialog("Ошибка!", "Не правильный логин или пароль!").show();
				}
			});
			
			this.mainPane.setTop(menuBar);
		});*/
	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void updatePluginObjects(String key, Object object) {

	}

}
