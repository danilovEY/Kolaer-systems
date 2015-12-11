package ru.kolaer.client.javafx.plugins;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.ApplicationPlugin;

public class HWContent implements IApplication {

	private final BorderPane mainPane = new BorderPane();
	
	public HWContent() {
		this.mainPane.setCenter(new FlowPane(new Button("1"),new Button("2"), new Button("3")));
		this.mainPane.setPrefSize(800, 600);
	}
	
	@Override
	public Pane getContent() {
		return this.mainPane;
	}

	@Override
	public String getName() {
		return "Hello Wordl app!";
	}

}
