package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;
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
import ru.kolaer.client.javafx.plugins.UniformSystemLabel;
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
	
	private final UniformSystemLabel model;
	
	private final URLClassLoader classLoader;
	
	public VMLabelImpl(final UniformSystemLabel model) {
		this((URLClassLoader) VMLabelImpl.class.getClassLoader(), model);
	}
	
	public VMLabelImpl(final URLClassLoader classLoader, final UniformSystemLabel model) {
		super(Resources.V_LABEL);
		this.classLoader = classLoader;
		if(model == null) {
			final NullPointerException ex = new NullPointerException("Label не может быть null!");
			LOG.error("ILabel == null", ex);
			throw ex;
		}
		this.model = model;
		this.setUserData(this.model.getName());
		
		this.init();
	}
	
	private void init() {
		Platform.runLater(() -> {
			Thread.currentThread().setContextClassLoader(this.classLoader);
			Thread.currentThread().setName("Загрузка изображения ярлыка");
			this.labelName.setText(Optional.ofNullable(this.model.getName()).orElse("Ярлык"));
			final String icon = model.getIcon();
			if(icon != null && !icon.isEmpty()) {
				try(final InputStream iconInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(model.getIcon()))
				{
					LOG.debug("iconInputStream is null? = {}", iconInputStream == null ? true : false);
					if(iconInputStream!=null) {
						this.labelIcon.setImage(new Image(iconInputStream));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
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
		
	}

	@Override
	public UniformSystemLabel getModel() {
		return this.model;
	}

	@Override
	public EventHandler<ActionEvent> getOnAction() {
		return this.labelRun.getOnAction();
	}
}
