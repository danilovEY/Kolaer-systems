package ru.kolaer.client.javafx.plugins;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.plugins.ILabel;

public class HWLabel implements ILabel {
	private final BorderPane mainPane = new BorderPane();
	
	public HWLabel() {
		mainPane.setPrefSize(100, 100);
		mainPane.setStyle("-fx-background-color: red");
	}

	@Override
	public String getName() {
		return "Hello World!";
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public Pane getIcon() {
		return mainPane;
	}

	@Override
	public void setIcon(Pane pane) {
		
	}

}
