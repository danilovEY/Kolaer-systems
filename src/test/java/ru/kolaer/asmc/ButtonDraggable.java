package ru.kolaer.asmc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ButtonDraggable extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	 Button btOK = new Button("OK");
         Pane pane = new Pane();
         // this code drags the button
         btOK.setOnMouseDragged(e -> {
         btOK.setLayoutX(e.getSceneX());
         btOK.setLayoutY(e.getSceneY());
          });
         // button added to pane and pane added to scene
         pane.getChildren().add(btOK);
         Scene scene = new Scene(pane,200, 250);
         primaryStage.setTitle("A Draggable button");
         primaryStage.setScene(scene);
         primaryStage.show();

    }

    // records relative x and y co-ordinates.
    class Delta {
        double x, y;
    }

    public static void main(String args[]) {
        launch(args);
    }

}