package ru.kolaer.asmc.runnable;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.tools.serializations.SerializationObjects;
import ru.kolaer.asmc.ui.javafx.controller.CMainFrame;

public class JavaFXLauncher extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		SerializationObjects ser = new SerializationObjects();
		SettingSingleton.setInstance(ser.getSerializeSetting());
		SettingSingleton.getInstance().setSerializationGroups(ser);	
		
		if(!this.getParameters().getNamed().isEmpty()) {
			String passRoot = this.getParameters().getNamed().get("root_set");
			if(SettingSingleton.getInstance().getRootPass().equals(passRoot)){
				SettingSingleton.getInstance().setRoot(true);
			}
		}
		
		new CMainFrame().start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
