package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMLabel;
import ru.kolaer.client.javafx.plugins.ILabel;
import ru.kolaer.client.javafx.tools.Resources;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class VMLabelImpl extends ImportFXML implements VMLabel {
	private static final Logger LOG = LoggerFactory.getLogger(VMLabelImpl.class);
	
	@FXML
	private Button labelRun;
	@FXML
	private Label labelName;
	@FXML
	private ImageView labelIcon;
	
	private final ILabel model;
	
	public VMLabelImpl(final ILabel model) {
		super(Resources.V_LABEL);
		if(model == null) {
			LOG.error("ILabel == null");
			throw new RuntimeException("Label не может быть null!");
		}
		this.model = model;
		this.setUserData(this.model.getName());
	}
	
	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Pane getContent() {
		return this;
	}

	@Override
	public void setOnAction(final EventHandler<ActionEvent> value) {
		this.labelRun.setOnAction(value);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		Platform.runLater(() -> {
			this.labelName.setText(this.model.getName());
			final URL urlIconLabel = ClassLoader.getSystemClassLoader().getResource(model.getIcon());
			LOG.debug("urlIconLabel: {}", urlIconLabel);
			this.labelIcon.setImage(new Image(urlIconLabel.toString()));
		});
	}

	@Override
	public ILabel getModel() {
		return this.model;
	}

	@Override
	public EventHandler<ActionEvent> getOnAction() {
		return this.labelRun.getOnAction();
	}
}
