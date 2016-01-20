package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.presenter.impl.PWindowPluginImpl;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerWindowsObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerWindowsObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.mvp.viewmodel.VMStartButton;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.tools.Resources;

public class VMExplorerImpl extends ImportFXML implements VMExplorer, ExplorerWindowsObresvable, ExplorerWindowsObserver {	
    private static final Logger LOG = LoggerFactory.getLogger(VMExplorerImpl.class);
	
	@FXML
    private Pane desktop;
    @FXML
    private BorderPane taskPane;
    @FXML
    private FlowPane desktopWithLabels;
    @FXML
    private HBox taskPaneWithApp;
	
    private final Set<ExplorerWindowsObserver> observerSet = new HashSet<>();
    private final Set<ExplorerWindowsObresvable> pluginsSet = new HashSet<>();
    
    
    
	public VMExplorerImpl() {
		super(Resources.V_EXPLORER);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		Platform.runLater(() -> {
			final VMStartButton startButton = new VMStartButtonImpl(this.desktop);	
			this.taskPane.setLeft(startButton.getContent());
		});
		
		this.desktop.heightProperty().addListener((observable, oldValue, newValue) -> {
			this.desktopWithLabels.setPrefHeight(desktop.getHeight());
		});
		
		this.desktop.widthProperty().addListener((observable, oldValue, newValue) -> {
			this.desktopWithLabels.setPrefWidth(desktop.getWidth());
		});
	}

	@Override
	public void addPlugin(final IKolaerPlugin plugin) {
		this.addPlugin(plugin, (URLClassLoader) this.getClass().getClassLoader());
	}
	

	@Override
	public void addPlugin(final IKolaerPlugin plugin, final URLClassLoader jarClassLoaser) {
		final ExecutorService threadFroLoadPlug = Executors.newSingleThreadExecutor();
		
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setName("Инициализация плагина: " + plugin.getName());
			Thread.currentThread().setContextClassLoader(jarClassLoaser);
			
			final PWindowPluginImpl plg = new PWindowPluginImpl( jarClassLoaser, plugin, this.taskPaneWithApp);
			plg.registerObserver(this);
			
			this.pluginsSet.add(plg);
			
			return plg;
		}, threadFroLoadPlug).exceptionally((t) -> {
			LOG.error("Ошибка при инициализации плагина!", t);
			return null;
		}).thenAcceptAsync(plg -> {
			Platform.runLater(() -> {
				Thread.currentThread().setName("Добавления ярлыка на explorer плагина: " + plugin.getName());
				Thread.currentThread().setContextClassLoader(jarClassLoaser);
				
				this.desktopWithLabels.getChildren().add(plg.getVMLabel().getContent());
				
				if(this.desktopWithLabels.getChildren().size() > 1) {
					ObservableList<Node> workingCollection = FXCollections.observableArrayList(this.desktopWithLabels.getChildren());

					Collections.sort(workingCollection, (node1, node2) -> {
						return String.CASE_INSENSITIVE_ORDER.compare(node1.getUserData().toString(), node2.getUserData().toString());
					});
					this.desktopWithLabels.getChildren().setAll(workingCollection);
				}
				threadFroLoadPlug.shutdown();
			});
		}, threadFroLoadPlug).exceptionally((t) -> {
			LOG.error("Ошибка при добавлении ярлыка!", t);
			return null;
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

	@Override
	public void removeAll() {
		this.desktopWithLabels.getChildren().clear();
		this.taskPaneWithApp.getChildren().clear();
	}

	@Override
	public void notifyOpenWindow(final PWindow window) {
		this.observerSet.parallelStream().forEach(obs -> obs.updateOpenWindow(window));
	}

	@Override
	public void notifyCloseWindow(final PWindow window) {
		this.observerSet.parallelStream().forEach(obs -> obs.updateCloseWindow(window));
	}

	@Override
	public void registerObserver(final ExplorerWindowsObserver observer) {
		this.observerSet.add(observer);
	}

	@Override
	public void removeObserver(final ExplorerWindowsObserver observer) {
		this.observerSet.remove(observer);
	}

	@Override
	public void updateOpenWindow(PWindow window) {
		this.notifyOpenWindow(window);
	}

	@Override
	public void updateCloseWindow(PWindow window) {
		this.notifyCloseWindow(window);
	}
}
