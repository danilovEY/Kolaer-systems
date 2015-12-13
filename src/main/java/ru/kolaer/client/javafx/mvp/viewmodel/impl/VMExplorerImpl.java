package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Pane desktop;

    @FXML
    private Button startButton;

    @FXML
    private BorderPane taskPane;

    @FXML
    private FlowPane desktopWithLabels;
	
    private final Map<IKolaerPlugin, VMLabel> mapPlugin = new HashMap<>();
    
	public VMExplorerImpl() {
		super(Resources.V_EXPLORER);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		desktop.heightProperty().addListener((observable, oldValue, newValue) -> {
			desktopWithLabels.setPrefHeight(desktop.getHeight());
		});
		
		desktop.widthProperty().addListener((observable, oldValue, newValue) -> {
				desktopWithLabels.setPrefWidth(desktop.getWidth());
		});
	}

	@Override
	public void addPlugin(IKolaerPlugin plugin) {
		ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			final VMLabel runnLabel = new VMLabelImpl(plugin.getLabel());
			final VMLabel runnLabel2 = new VMLabelImpl(plugin.getLabel());
			final VMLabel runnLabel3 = new VMLabelImpl(plugin.getLabel());
			final VMLabel runnLabel4 = new VMLabelImpl(plugin.getLabel());
			final VMLabel runnLabel5 = new VMLabelImpl(plugin.getLabel());
			final VMLabel runnLabel6 = new VMLabelImpl(plugin.getLabel());
			runnLabel.setOnAction(e -> {
				Platform.runLater(() -> {
					final PCustomWindow window = new PCustomWindowImpl(this.desktop, plugin.getApplication(), plugin.getName());
					window.show();
				});	
			});
			
			Platform.runLater(() -> {
				this.desktopWithLabels.getChildren().add(runnLabel.getContent());
				this.desktopWithLabels.getChildren().add(runnLabel2.getContent());
				this.desktopWithLabels.getChildren().add(runnLabel3.getContent());
				this.desktopWithLabels.getChildren().add(runnLabel4.getContent());
				this.desktopWithLabels.getChildren().add(runnLabel5.getContent());
				this.desktopWithLabels.getChildren().add(runnLabel6.getContent());
				
			});	
			
			mapPlugin.put(plugin, runnLabel);
		});
		thread.shutdown();
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
