package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ru.kolaer.asmc.tools.Resources;

public class CGroupLabels extends AnchorPane implements Initializable{
	
	private static final Logger LOG = LoggerFactory.getLogger(CGroupLabels.class);
	
	@FXML
	private AnchorPane mainPanel;
	
	@FXML
	private Label nameGroupLabels;
	
	public CGroupLabels(){
    	FXMLLoader loader = new FXMLLoader(Resources.V_GROUP_LABELS);
    	loader.setRoot(this);
    	loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOG.error("Не найден view: " + Resources.V_GROUP_LABELS, e);
			throw new RuntimeException(e);
		}
	}
	
	public AnchorPane getView() {
		return mainPanel;
	}

	public void setText(String text){
		this.nameGroupLabels.setText(text);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
