package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
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

	}

	@Override
	public void addPlugin(final IKolaerPlugin plugin) {
		Platform.runLater(() -> {		
			Thread.currentThread().setName("Инициализация плагинов");
			final PPlugin plg = new PPluginImpl(plugin, this.taskPaneWithApp);
			this.desktopWithLabels.getChildren().add(plg.getVMLabel().getContent());
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
