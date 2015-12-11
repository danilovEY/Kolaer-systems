package ru.kolaer.client.javafx.mvp.presenter.impl;

import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.presenter.PMainFrame;
import ru.kolaer.client.javafx.mvp.view.VMainFrame;
import ru.kolaer.client.javafx.mvp.view.impl.VMainFrameImpl;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PMainFrameImpl implements PMainFrame{

	private VMainFrame view;
	
	/**
	 * {@linkplain PMainFrameImpl}
	 */
	public PMainFrameImpl() {
		this.view = new VMainFrameImpl();
	}
	
	/**
	 * {@linkplain PMainFrameImpl}
	 */
	public PMainFrameImpl(Stage stage) {
		view = new VMainFrameImpl(stage);
	}
	
	@Override
	public void show() {
		this.view.setVisible(true);
	}

	@Override
	public void close() {
		this.view.setVisible(false);
	}

	@Override
	public VMainFrame getView() {
		return this.view;
	}

	@Override
	public void setView(VMainFrame view) {
		this.view = view;
	}
}
