package ru.kolaer.client.javajx.plugins.helloworld;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.Pane;
import ru.kolaer.client.javajx.plugins.Application;
import ru.kolaer.client.javajx.plugins.DesktopLabel;
import ru.kolaer.client.javajx.plugins.Label;

@DesktopLabel
public class HelloWorldLabel implements Label {
	private final BorderPane mainPane = new BorderPane();
	
	public HelloWorldLabel() {
		mainPane.setPrefSize(100, 100);
		mainPane.setStyle("-fx-background-color: red");
	}

	@Override
	public Application getApplication() {
		return new HelloWorldApplication();
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
