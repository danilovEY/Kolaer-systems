package ru.kolaer.client.javafx.mvp.presenter.impl;

import ru.kolaer.client.javafx.mvp.presenter.PLabel;
import ru.kolaer.client.javafx.mvp.view.VLabel;
import ru.kolaer.client.javafx.mvp.view.impl.VLabelImpl;
import ru.kolaer.client.javafx.plugins.ILabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PLabelImpl implements PLabel {
	private VLabel view = new VLabelImpl();
	private ILabel labelModel;
	
	/**
	 * {@linkplain PLabelImpl}
	 */
	public PLabelImpl() {
		this(null);
	}
	
	public PLabelImpl(ILabel label) {
		this.setModel(label);
	}

	@Override
	public VLabel getView() {
		return this.view;
	}

	@Override
	public void setView(VLabel view) {
		this.view = view;
	}

	@Override
	public ILabel getModel() {
		return this.labelModel;
	}

	@Override
	public void setModel(ILabel model) {
		this.labelModel = model;
		this.view.updateLable(model);
	}
}
