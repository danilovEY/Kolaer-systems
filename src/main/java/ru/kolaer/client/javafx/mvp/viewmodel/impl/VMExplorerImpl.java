package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PPlugin;
import ru.kolaer.client.javafx.mvp.presenter.impl.PPluginImpl;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
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
    @FXML
    private HBox taskPaneWithApp;
	
    //private final Map<IKolaerPlugin, VMLabel> mapPlugin = new HashMap<>();
    
	public VMExplorerImpl() {
		super(Resources.V_EXPLORER);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		desktop.heightProperty().addListener((observable, oldValue, newValue) -> {
			desktopWithLabels.setPrefHeight(desktop.getHeight());
		});
		
		desktop.widthProperty().addListener((observable, oldValue, newValue) -> {
				desktopWithLabels.setPrefWidth(desktop.getWidth());
		});
	}

	@Override
	public void addPlugin(final IKolaerPlugin plugin) {
		Platform.runLater(() -> {		
			Thread.currentThread().setName("Инициализация плагинов");
			final PPlugin plg = new PPluginImpl(plugin, this.taskPaneWithApp, desktop);
			this.desktopWithLabels.getChildren().add(plg.getVMLabel().getContent());
			
			if(this.desktopWithLabels.getChildren().size() > 1) {
				ObservableList<Node> workingCollection = FXCollections.observableArrayList(this.desktopWithLabels.getChildren());

				Collections.sort(workingCollection, (node1, node2) -> {
					return String.CASE_INSENSITIVE_ORDER.compare(node1.getUserData().toString(), node2.getUserData().toString());
				});
				this.desktopWithLabels.getChildren().setAll(workingCollection);
			}				
		});
	}
	
	@Override
	public void removePlugin(final IKolaerPlugin plugin) {
		
	}

	@Override
	public Pane getContent() {
		return this;
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}
}
