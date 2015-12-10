package ru.kolaer.client.javajx.mvp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javajx.plugins.helloworld.HelloWorldLabel;
import ru.kolaer.client.javajx.tools.Resources;

public class VMainFrame extends Application {
	private final BorderPane mainPanel = new BorderPane();
	private final Explorer explorer = new Explorer();
	
	@Override
	public void start(Stage stage) throws Exception {
		this.initialization(); 
		
		stage.setScene(new Scene(mainPanel, 800, 600));
		stage.centerOnScreen();
		stage.setMaximized(true);
		stage.show();
	}
	
	private void initialization() {	
		final Menu fileMenu = new Menu(Resources.L_MENU_FILE);
		
		this.explorer.addLabel(new HelloWorldLabel());
		
		this.mainPanel.setTop(new MenuBar(fileMenu));
		this.mainPanel.setCenter(this.explorer);
	}

}
