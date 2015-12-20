package ru.kolaer.asmc.runnable;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.client.javafx.plugins.IApplication;

public class ASMCApplication extends Application implements IApplication {
	private AnchorPane root;
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
	public void run() {
		SettingSingleton.initialization();
		try {
			root = FXMLLoader.load(Resources.V_MAIN_FRAME);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	}

}
