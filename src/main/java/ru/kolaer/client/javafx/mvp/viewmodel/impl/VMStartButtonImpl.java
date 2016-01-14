package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMStartButton;
import ru.kolaer.client.javafx.mvp.viewmodel.VMStartPane;
import ru.kolaer.client.javafx.tools.Resources;

public class VMStartButtonImpl extends ImportFXML implements VMStartButton{

	@FXML
	private Button startButton;
	private final VMStartPane vStartPane = new VMStartPaneImpl();
	private final Pane desktop;
	
	public VMStartButtonImpl(final Pane desktop) {
		super(Resources.V_START_BUTTON);
		this.desktop = desktop;
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {	
		Platform.runLater(() -> {
			final Parent vStartPaneFx = vStartPane.getContent();
			this.desktop.getChildren().add(vStartPaneFx);
			vStartPaneFx.setLayoutX(0);
			vStartPaneFx.setLayoutY(desktop.getHeight() - 400);
		});
		
		Platform.runLater(() -> {
			final ImageView image = new ImageView(Resources.ICON_START_BUTTON.toString());
			image.setFitHeight(50);
			image.setFitWidth(50);
			this.startButton.setStyle("-fx-background-color: transparent;");
			this.startButton.setGraphic(image);
		});
		
		this.startButton.setOnAction(e -> {
			if(vStartPane.isShow())
				vStartPane.close();
			else
				vStartPane.show();
		});
		
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Parent getContent() {
		return this;
	}

}
