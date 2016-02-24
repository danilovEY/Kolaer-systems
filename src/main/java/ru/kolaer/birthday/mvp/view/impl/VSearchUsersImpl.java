package ru.kolaer.birthday.mvp.view.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.view.VSearchUsers;

public class VSearchUsersImpl implements VSearchUsers {
	private BorderPane mainPane;
	private Button startSearchBut;
	private TextField searchField;
	
	public VSearchUsersImpl() {
		this.init();
	}
	
	private void init() {
		this.mainPane = new BorderPane();
		
		this.searchField = new TextField();	
		
		this.startSearchBut = new Button("Поиск");
		this.startSearchBut.defaultButtonProperty().bind(this.searchField.focusedProperty());
		
		this.mainPane.setCenter(searchField);
		this.mainPane.setRight(startSearchBut);
		BorderPane.setAlignment(this.startSearchBut, Pos.CENTER);
	}
	
	@Override
	public Pane getViewPane() {
		return this.mainPane;
	}

	@Override
	public void setSearchAction(final EventHandler<ActionEvent> value) {
		this.startSearchBut.setOnAction(value);
	}

	@Override
	public String getSearchText() {
		return this.searchField.getText();
	}
}
