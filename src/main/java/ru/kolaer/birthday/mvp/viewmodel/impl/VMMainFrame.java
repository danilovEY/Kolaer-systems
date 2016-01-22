package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;import java.util.PropertyPermission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("restriction")
public class VMMainFrame extends Application {
	private final URL FXML_VIEW = VMMainFrame.class.getResource("/birthdayView/VMainFrame.fxml");
	private final Logger LOG = LoggerFactory.getLogger(VMMainFrame.class);
	
	@FXML
	private TableView<String> userBirthdayTable;
	@FXML
	private ScrollPane scrollTablePane;
	
	@FXML
	public void initialize() {
		this.scrollTablePane.heightProperty().addListener((observable, oldValue, newValue) -> {
			this.userBirthdayTable.setPrefHeight(scrollTablePane.getHeight());
		});
		
		this.scrollTablePane.widthProperty().addListener((observable, oldValue, newValue) -> {
			this.userBirthdayTable.setPrefWidth(scrollTablePane.getWidth());
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			final Parent root = FXMLLoader.load(FXML_VIEW);
			primaryStage.setScene(new Scene(root, 800, 600));
		} catch(IOException ex) {
			LOG.error("Ошибка при чтении {}", FXML_VIEW, ex);
		}
		
		
		/*BorderPane pane = new BorderPane();
		DatePicker pick = new DatePicker();
		pick.setShowWeekNumbers(true);
	    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
	        @Override
	        public DateCell call(final DatePicker datePicker) {
	          return new DateCell() {
	            @Override
	            public void updateItem(LocalDate item, boolean empty) {
	              super.updateItem(item, empty);
	              setStyle("-fx-background-color: #FF0000;");
	              System.out.println(item);
	            }
	          };
	        }
	      };
	    pick.setDayCellFactory(dayCellFactory);
		DatePickerSkin skin = new DatePickerSkin(pick);
		skin.dispose();
		Node calendarControl = skin.getPopupContent();
		pane.setCenter(calendarControl);*/
		primaryStage.show();
			
	}

}
