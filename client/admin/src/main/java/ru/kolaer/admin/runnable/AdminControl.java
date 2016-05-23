package ru.kolaer.admin.runnable;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import ru.kolaer.admin.bid.mvp.model.BidModelManager;
import ru.kolaer.admin.bid.mvp.presenter.PBidTable;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;

public class AdminControl implements UniformSystemPlugin {
	private final String pass = "2serdce3";
	private UniformSystemEditorKit editorKid;
	private BorderPane mainPane;
	
	@Override
	public void setContent(Parent content) {
		
	}

	@Override
	public Parent getContent() {
		return mainPane;
	}

	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		this.editorKid = editorKid;
		Tools.runOnThreadFX(() -> {
			this.mainPane = new BorderPane();
		});
		
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
		Tools.runOnThreadFX(() -> {
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
		});
	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void updatePluginObjects(String key, Object object) {

	}

}
