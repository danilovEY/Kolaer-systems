package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;
import ru.kolaer.client.javafx.mvp.presenter.impl.PCustomWindowImpl;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.tools.Resources;

public class VMExplorerImpl extends ImportFXML implements VMExplorer {	
	
    @FXML
    private AnchorPane desktop;

    @FXML
    private Button startButton;

    @FXML
    private BorderPane taskPane;

    @FXML
    private FlowPane desktopWithLabels;
	
	public VMExplorerImpl() {
		super(Resources.V_EXPLORER);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	
	@Override
	public void addPlugin(IKolaerPlugin plugin) {
		final VMLabel runnLabel2 = new VMLabelImpl(plugin.getLabel());
		final VMLabel runnLabel = new VMLabelImpl(plugin.getLabel());
		runnLabel.setOnAction(e -> {
			Platform.runLater(() -> {
				final PCustomWindow window = new PCustomWindowImpl(plugin.getApplication(), plugin.getName());
				
				this.desktop.getChildren().add(window.getView().getWindow());
			});	
		});
		
		Platform.runLater(() -> {
			this.desktopWithLabels.getChildren().add(runnLabel.getContent());
			this.desktopWithLabels.getChildren().add(runnLabel2.getContent());
		});	
	}

	@Override
	public void removePlugin(IKolaerPlugin plugin) {

	}

	@Override
	public Pane getContent() {
		return this;
	}

	@Override
	public void setContent(Pane content) {
		this.setCenter(content);
	}
}
