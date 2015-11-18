package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.LabelModel;

public class CLabel extends BorderPane implements Initializable {
	
	@FXML
	private Button button;
	
	@FXML
	private ImageView image;
	
	@FXML
	private TextArea infoText;
	
	@FXML
	private Label nameLabel;
	
	private LabelModel label;
	
	public CLabel() {
		this.init();
	}
	
	public CLabel(LabelModel label){
		this.label = label;
		this.init();
	}
	
	private void init(){
		FXMLLoader loader = new FXMLLoader(Resources.V_LABEL);
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(this.label == null) return;
		
		this.button.setText(this.label.getName());
	}
}
