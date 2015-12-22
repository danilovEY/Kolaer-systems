package ru.kolaer.asmc.runnable;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.client.javafx.plugins.IApplication;

public class ASMCApplication extends Application implements IApplication {
	private final BorderPane root = new BorderPane();
	private AnchorPane pane;
	@Override
	public String getIcon() {
		return "resources/aerIcon.png";
	}

	@Override
	public Pane getContent() {
		return this.root;
	}

	@Override
	public String getName() {
		return "АСУП";
	}

	@Override
	public void stop() {
		// this.root = null;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	@Override
	public void run() {
		SettingSingleton.initialization();
		Platform.runLater(() -> {
			try{
				if(pane == null) {
					pane = FXMLLoader.load(Resources.V_MAIN_FRAME);
					root.setCenter(pane);
					//root.setPrefSize(800, 600);
				}
			}catch(MalformedURLException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		});
	}

}
