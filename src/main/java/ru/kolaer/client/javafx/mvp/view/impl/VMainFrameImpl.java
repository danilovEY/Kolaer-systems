package ru.kolaer.client.javafx.mvp.view.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.view.VMainFrame;
import ru.kolaer.client.javafx.tools.Resources;

public class VMainFrameImpl extends Application implements VMainFrame {
	private static final Logger LOG = LoggerFactory.getLogger(VMainFrameImpl.class);
	
	private Stage stage;
	private final BorderPane mainPanel = new BorderPane();
	
	
	/**
	 * {@linkplain VMainFrameImpl}
	 */
	public VMainFrameImpl() {
		this(new Stage());
	}
	
	/**
	 * {@linkplain VMainFrameImpl}
	 * @param stage
	 */
	public VMainFrameImpl(Stage stage) {
		this.start(Optional.ofNullable(stage).orElse(new Stage()));
	}

	@Override
	public void start(Stage stage) {
		this.initialization(); 
		this.stage = stage;
		this.stage.setScene(new Scene(this.mainPanel, 800, 600));
		this.stage.centerOnScreen();
		this.stage.setMaximized(true);
	}
	
	private void initialization() {		
		final Menu fileMenu = new Menu(Resources.L_MENU_FILE);
		
		this.mainPanel.setTop(new MenuBar(fileMenu));
		
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible)
			this.stage.show();
		else
			this.stage.hide();
	}

	@Override
	public Pane getContent() {
		return this.mainPanel;
	}

	@Override
	public void setTitle(String title) {
		this.stage.setTitle(title);
	}

	@Override
	public void setContent(Pane content) {
		this.mainPanel.setCenter(content);
	}
}