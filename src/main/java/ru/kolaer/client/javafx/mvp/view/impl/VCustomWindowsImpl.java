package ru.kolaer.client.javafx.mvp.view.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import jfxtras.labs.scene.control.window.Window;
import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.client.javafx.tools.Resources;

public class VCustomWindowsImpl implements VCustomWindow{
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	
	private final Window window = new Window();
	private double oldLayoutX = 0;
	private double oldLayoutY = 0;
	private double oldPrefWidth = 0;
	private double oldPrefHeight = 0;
	private boolean maximize = false;
	private boolean minimize = false;
	
	
	public VCustomWindowsImpl() {
		this.initialization();
	}

	private void initialization() {
		try {
			this.window.getStylesheets().setAll(Resources.WINDOW_CSS);
		} catch (NullPointerException ex) {
			LOG.error("CSS " + Resources.WINDOW_CSS + " не найден");
		}
		
		this.window.setLayoutX(100);
		this.window.setLayoutY(100);
	}
	
	@Override
	public Window getWindow() {
		return this.window;
	}

	@Override
	public Pane getContent() {
		return this.window.getContentPane();
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible) {
	        ScaleTransition st = new ScaleTransition();
	        st.setNode(this.window);
	        st.setFromX(0);
	        st.setFromY(0);
	        st.setToX(1);
	        st.setToY(1);
	        st.setDuration(Duration.seconds(0.2));
	        ObjectProperty<Transition> closeTransitionProperty =
	                new SimpleObjectProperty<>();
	        closeTransitionProperty.set(st);
	        closeTransitionProperty.get().play();
	        
		} else {
			this.window.close();
		}
	}

	@Override
	public void setTitle(String title) {
		this.window.setTitle(title);
	}

	@Override
	public void setContent(Pane content) {
		this.window.setPrefSize(content.getPrefWidth(), content.getPrefHeight());
		this.window.setContentPane(content);
	}

	@Override
	public void addRightWindowIcon(WindowIcon icon) {
		this.window.getRightIcons().add(icon);
	}

	@Override
	public void addLeftWindowIcon(WindowIcon icon) {
		this.window.getLeftIcons().add(icon);
	}

	@Override
	public void setMaximize(boolean max) {
		this.maximize = max;
		System.out.println("AA");
		if(this.maximize) {
			this.oldLayoutX = this.window.getLayoutX();
			this.oldLayoutY = this.window.getLayoutY();
			this.oldPrefWidth = this.window.getPrefWidth();
			this.oldPrefHeight = this.window.getPrefHeight();
			
			Pane parent = (Pane)this.window.getParent();
			this.window.setLayoutX(parent.getLayoutX());
			this.window.setLayoutY(parent.getLayoutY());
			this.window.setPrefSize(parent.getWidth(), parent.getHeight());
		} else {
			this.window.setLayoutX(this.oldLayoutX);
			this.window.setLayoutY(this.oldLayoutY);
			this.window.setPrefSize(this.oldPrefWidth, this.oldPrefHeight);
		}
		
		
	}

	@Override
	public void setMinimize(boolean min) {
		this.minimize = min;
	}

	@Override
	public boolean isMaximize() {
		return this.maximize;
	}

	@Override
	public boolean isMinimize() {
		return this.minimize;
	}
}