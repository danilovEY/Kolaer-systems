package ru.kolaer.birthday.mvp.view.impl;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;

public class VTableWithUsersBirthdayImpl implements VTableWithUsersBirthday {
	private final BorderPane tablePane = new BorderPane();
	private final TableView<UserModel> userBirthdayTable = new TableView<UserModel>();
	
	public VTableWithUsersBirthdayImpl() {
		this.init();
	}
	
	@SuppressWarnings("unchecked")
	private void init() {		
		this.userBirthdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		final TableColumn<UserModel, URL> userIconColumn = new TableColumn<>("Фотография");
	    userIconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
	    userIconColumn.setCellFactory((TableColumn<UserModel, URL> param) -> {
	    	return new TableCell<UserModel, URL>(){
		            @Override
		            public void updateItem(final URL item, final boolean empty) { 
		            	if(!empty) {
			                URL tempIconUrl = item;
			            	if(item == null){   
			            		tempIconUrl = this.getClass().getResource("/resources/nonePicture.jpg");
			                }
			                
		                    final ImageView imageview = new ImageView();
		                    imageview.setFitHeight(100);
		                    imageview.setFitWidth(100);
		                    imageview.setImage(new Image(tempIconUrl.toString(), true));
		                    
		                    this.setGraphic(new BorderPane(imageview));
		            	}
		            }
		        };           	       
	    });
	    
	    final TableColumn<UserModel, Integer> userPersonNumberColumn = new TableColumn<>("Таб. номер");
	    userPersonNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personNumber"));
	    
	    final TableColumn<UserModel, String> userFirstNameColumn = new TableColumn<>("Имя");
	    userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    
	    final TableColumn<UserModel, String> userSecondNameColumn = new TableColumn<>("Фамилия");
	    userSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
	    
	    final TableColumn<UserModel, String> userThirdNameColumn = new TableColumn<>("Отчество");
	    userThirdNameColumn.setCellValueFactory(new PropertyValueFactory<>("thirdName"));
	    
	    final TableColumn<UserModel, Date> userBirthdayColumn = new TableColumn<>("Дата рождения");
	    userBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
	    userBirthdayColumn.setCellFactory(film -> {
	    	 return new TableCell<UserModel, Date>() {
	    	        @Override
	    	        protected void updateItem(Date item, boolean empty) {
	    	            super.updateItem(item, empty);

	    	            if (item == null || empty) {
	    	                setText(null);
	    	            } else {
	    	            	final SimpleStringProperty property = new SimpleStringProperty();
	    	    	    	final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	    	    	    	property.setValue(dateFormat.format(item));
	    	    	    	
	    	                setText(property.getValue());
	    	            }
	    	        }
	    	    };
    	});
	    
	    final TableColumn<UserModel, String> userDepartamentColumn = new TableColumn<>("Цех/Отдел");
	    userDepartamentColumn.setCellValueFactory(new PropertyValueFactory<>("departament"));
	    
	    userIconColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userFirstNameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userSecondNameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userThirdNameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userBirthdayColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userDepartamentColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userPersonNumberColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    
		this.userBirthdayTable.getColumns().addAll(userIconColumn, userPersonNumberColumn, userSecondNameColumn, userFirstNameColumn,
				userThirdNameColumn,
				userDepartamentColumn,
				userBirthdayColumn);
		
		this.tablePane.setCenter(this.userBirthdayTable);
	}

	@Override
	public Pane getViewPane() {
		return this.tablePane;
	}

	@Override
	public void setData(final ObservableList<UserModel> userList) {
		this.userBirthdayTable.setItems(userList);
	}
}
