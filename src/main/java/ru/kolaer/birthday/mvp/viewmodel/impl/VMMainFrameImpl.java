package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;

@SuppressWarnings("unchecked")
public class VMMainFrameImpl extends Application implements VMMainFrame {
	private final URL FXML_VIEW = VMMainFrameImpl.class.getResource("/birthdayView/VMainFrame.fxml");
	private final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	
	@FXML
	private ScrollPane scrollTablePane;
	@FXML
	private FlowPane calendarPane;
	
	@FXML
	public void initialize() {
		final TableView<UserModel> userBithdayTable = new TableView<UserModel>();
		userBithdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.scrollTablePane.setContent(userBithdayTable);
		
		this.scrollTablePane.heightProperty().addListener((observable, oldValue, newValue) -> {
			userBithdayTable.setPrefHeight(scrollTablePane.getHeight());
		});
		
		this.scrollTablePane.widthProperty().addListener((observable, oldValue, newValue) -> {
			userBithdayTable.setPrefWidth(scrollTablePane.getWidth());
		});
		
		final TableColumn<UserModel, URL> userIconColumn = new TableColumn<>("Фотография");
	    userIconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
	    userIconColumn.setCellFactory((TableColumn<UserModel, URL> param) -> {
				TableCell<UserModel, URL> cell = new TableCell<UserModel, URL>(){
		            @Override
		            public void updateItem(final URL item, final boolean empty) { 
		            	if(!empty) {
			                URL tempIconUrl = item;
			            	if(item == null){   
			            		tempIconUrl = this.getClass().getResource("/nonePicture.jpg");
			                }
			                
		                    final ImageView imageview = new ImageView();
		                    imageview.setFitHeight(100);
		                    imageview.setFitWidth(100);
		                    imageview.setImage(new Image(tempIconUrl.toString(), true));
		                    
		                    setGraphic(new BorderPane(imageview));
		            	}
		            }
		        };           
		        return cell;	       
	    });
	    
	   
	    
	    final TableColumn<UserModel, Integer> userPersonNumberColumn = new TableColumn<>("Таб. номер");
	    userPersonNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personNumber"));
	    
	    final TableColumn<UserModel, String> userFirstNameColumn = new TableColumn<>("Имя");
	    userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    
	    final TableColumn<UserModel, String> userSecondNameColumn = new TableColumn<>("Фамилия");
	    userSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
	    
	    final TableColumn<UserModel, String> userThirdNameColumn = new TableColumn<>("Отчество");
	    userThirdNameColumn.setCellValueFactory(new PropertyValueFactory<>("thirdName"));
	    
	    final TableColumn<UserModel, String> userBithdayColumn = new TableColumn<>("Дата рождения");
	    userBithdayColumn.setCellValueFactory(new PropertyValueFactory<>("bithday"));
	    userBithdayColumn.setCellValueFactory(
	    		   film -> {
	    			      SimpleStringProperty property = new SimpleStringProperty();
	    			      DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	    			      property.setValue(dateFormat.format(film.getValue().getBithday()));
	    			      return property;
	    			   });
	    
	    final TableColumn<UserModel, String> userDepartamentColumn = new TableColumn<>("Цех/Отдел");
	    userDepartamentColumn.setCellValueFactory(new PropertyValueFactory<>("departament"));
	    
	    userIconColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userFirstNameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userSecondNameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userThirdNameColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userBithdayColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userDepartamentColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    userPersonNumberColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
	    
		userBithdayTable.getColumns().addAll(userIconColumn, userPersonNumberColumn, userFirstNameColumn, userSecondNameColumn,
				userThirdNameColumn,
				userDepartamentColumn,
				userBithdayColumn);
		
		this.calendarPane.getChildren().add(new VMCalendarImpl().getView().getViewPane());
	    /*final ObservableList<UserModel> data =
	            FXCollections.observableArrayList(new UserModelImpl(new Integer(333), "AAAAAAA", "B", "C", "D", new Date(), null));
	    userBithdayTable.setItems(data);*/
		
		/*RestTemplate temp = new RestTemplate();
		DbUsers1c[] users = temp.getForObject("http://localhost:8080/kolaer/database/user1c/get/users/max/5", DbUsers1c[].class);
		for(DbUsers1c u : users)
			System.out.println(u);*/
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {	
		try {
			final Parent root = FXMLLoader.load(FXML_VIEW);
			primaryStage.setScene(new Scene(root, 800, 600));
		} catch(IOException ex) {
			LOG.error("Ошибка при чтении {}", FXML_VIEW, ex);
		}
		
		primaryStage.show();		
	}
}
