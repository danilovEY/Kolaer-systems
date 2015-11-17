package ru.kolaer.asmc.ui.javafx.presenter;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;

public class PMainFrame extends Application {
	
	@FXML
	private VBox navigatePanel;
	
	@FXML
	private FlowPane contentPanel;
	
    @FXML
    public void initialize(){

    }
    
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(Resources.V_MAIN_FRAME);
		primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        
        primaryStage.show();
	}
}