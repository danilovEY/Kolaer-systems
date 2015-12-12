package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.view.VLabel;
import ru.kolaer.client.javafx.plugins.ILabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class VLabelImpl implements VLabel {

	private final BorderPane mainPane = new BorderPane();
	private final Button rannableButton = new Button();
	/**
	 * {@linkplain VLabelImpl}
	 */
	public VLabelImpl() {
		this.initialization();
	}
	
	private void initialization() {
		mainPane.setPrefSize(100, 100);
		this.mainPane.setCenter(this.rannableButton);
	}
	
	@Override
	public void setContent(Pane content) {
		this.rannableButton.setGraphic(content);
	}

	@Override
	public Pane getContent() {
		return mainPane;
	}

	@Override
	public void updateLable(ILabel label) {
		if(label == null)
			return;
		
		this.mainPane.setBottom(new Label(label.getName()));
		this.setContent(label.getIcon());
	}

	@Override
	public void setOnAction(EventHandler<ActionEvent> value) {
		this.rannableButton.setOnAction(value);
	}

}
