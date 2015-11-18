package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.GroupLabelsModel;

public class CGroupLabels extends BorderPane implements Initializable, Observable{
	
	private static final Logger LOG = LoggerFactory.getLogger(CGroupLabels.class);
	
	private GroupLabelsModel groupModel;
	private final List<Observer> observerList = new ArrayList<>();
	
	@FXML
	private Button button;
	
	public CGroupLabels(){
		this.init();
	}
	
	public CGroupLabels(GroupLabelsModel group) {
		this.groupModel = group;
		this.init();
	}
	
	private void init(){
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

	public void setText(String text){
		this.button.setText(text);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(this.groupModel!=null){
			this.setText(this.groupModel.getNameGroup());
		}

		this.button.setOnMouseClicked((e)->this.notifyObserverClick());
	}

	@Override
	public void notifyObserverClick() {
		observerList.forEach((o) -> o.updateClick(this.groupModel));
	}

	@Override
	public void registerOberver(Observer observer) {
		observerList.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observerList.remove(observer);
	}
}
