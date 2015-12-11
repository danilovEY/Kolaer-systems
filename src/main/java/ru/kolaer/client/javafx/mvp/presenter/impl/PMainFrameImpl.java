package ru.kolaer.client.javafx.mvp.presenter.impl;

import ru.kolaer.client.javafx.mvp.presenter.PMainFrame;
import ru.kolaer.client.javafx.mvp.view.VMainFrame;
import ru.kolaer.client.javafx.mvp.view.impl.VMainFrameImpl;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PMainFrameImpl implements PMainFrame{

	private VMainFrame view = new VMainFrameImpl();
	
	/**
	 * {@linkplain PMainFrameImpl}
	 */
	public PMainFrameImpl() {
		
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
