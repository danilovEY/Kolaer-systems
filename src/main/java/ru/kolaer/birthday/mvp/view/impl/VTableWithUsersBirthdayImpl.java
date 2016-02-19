package ru.kolaer.birthday.mvp.view.impl;

import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;

/**
 * View - таблици с днями рождениями.
 *
 * @author danilovey
 * @version 0.1
 */
public class VTableWithUsersBirthdayImpl implements VTableWithUsersBirthday {
	private final Logger LOG = LoggerFactory.getLogger(VTableWithUsersBirthdayImpl.class);
	/**Модель объектов для таблици.*/
	private final ObservableList<UserModel> tableModel = FXCollections.observableArrayList();
	/**Панель с таблицой.*/
	private final BorderPane tablePane = new BorderPane();
	/**Таблица*/
	private final TableView<UserModel> userBirthdayTable = new TableView<UserModel>();
	/**Заголовок таблици.*/
	private final Label titleLabel = new Label();
	/**Формат месяцев.*/
	private final DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){
		private static final long serialVersionUID = -1561907971611705068L;
		@Override
        public String[] getMonths() {
            return new String[]{"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
                "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
        }      
    };
	
	public VTableWithUsersBirthdayImpl() {
		Platform.runLater(() -> {
			this.init();
		});
	}
	
	@SuppressWarnings("unchecked")
	private void init() {		
		this.userBirthdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);		
		this.userBirthdayTable.setItems(tableModel);
		this.userBirthdayTable.getSelectionModel().setCellSelectionEnabled(true);
		this.userBirthdayTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.userBirthdayTable.setEditable(true);
		
		final MenuItem item = new MenuItem("Копировать");
	    item.setOnAction(event -> {
            final StringBuilder clipboardString = new StringBuilder();
            final Iterator<Integer> iter = userBirthdayTable.getSelectionModel().getSelectedIndices().iterator();
            while(iter.hasNext()) {
            	int index = iter.next();
	            for( final TablePosition<UserModel, Object> col : userBirthdayTable.getSelectionModel().getSelectedCells()){
	            	if(col.getRow() == index) {
	            		final ObservableValue<Object> observableValue = col.getTableColumn().getCellObservableValue(index);
		            	if(observableValue != null) {
		            		if(observableValue.getValue().getClass() == Date.class) {
		            			final SimpleStringProperty property = new SimpleStringProperty();
		    	    	    	final DateFormat dateFormat = new SimpleDateFormat("dd MMMMM", myDateFormatSymbols);
		    	    	    	property.setValue(dateFormat.format((Date)observableValue.getValue()));
		    	    	    	clipboardString.append(property.getValue()).append(" ");
		            		} else {
		            			clipboardString.append(observableValue.getValue()).append(" ");
		            		}
		            	}
	            	}
	            }
	            clipboardString.deleteCharAt(clipboardString.length()-1).append('\n');
            }
            final ClipboardContent content = new ClipboardContent();
            //В буфер
            content.putString(clipboardString.toString());
            Clipboard.getSystemClipboard().setContent(content);
	    });

	    this.userBirthdayTable.setContextMenu(new ContextMenu(item));
		
		final TableColumn<UserModel, String> userIconColumn = new TableColumn<>("Фотография");
	    userIconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
	    userIconColumn.setCellFactory((TableColumn<UserModel, String> param) -> {
	    	return new TableCell<UserModel, String>(){
	            @Override
	            public void updateItem(final String item, final boolean empty) { 
	            	Platform.runLater(() -> {
		            	this.setGraphic(null);
		            	if(!empty) {
		                    final ImageView imageview = new ImageView();
		                    imageview.setFitHeight(100);
		                    imageview.setFitWidth(116);
		                    
			            	if(item == null){   
			            		final URL url = this.getClass().getResource("/nonePicture.jpg");
			            		imageview.setImage(new Image(url.toString(), true));
			                } else {                	
								try{
									//Берем фотки с местного сайта
									final StringBuilder pathToIcon = new StringBuilder("http://asupkolaer/app_ie8/assets/images/vCard/o_").append(URLEncoder.encode(item, "UTF-8"));
				                	imageview.setImage(new Image(pathToIcon.toString().replaceAll("\\+", "%20"), true));
								}catch(Exception e){
									LOG.error("Ошибка при конвертации кодировки URL!", e);
								}
			                }       	
		                    this.setGraphic(new BorderPane(imageview));
		            	}
	            	});
	            }
	        };           	       
	    });
	    
	    final TableColumn<UserModel, Integer> userOrganizationColumn = new TableColumn<>("Организация");
	    userOrganizationColumn.setCellValueFactory(new PropertyValueFactory<>("organization"));
	    
	    final TableColumn<UserModel, String> userFirstNameColumn = new TableColumn<>("Имя");
	    userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    
	    final TableColumn<UserModel, String> userSecondNameColumn = new TableColumn<>("Фамилия");
	    userSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
	    
	    final TableColumn<UserModel, String> userThirdNameColumn = new TableColumn<>("Отчество");
	    userThirdNameColumn.setCellValueFactory(new PropertyValueFactory<>("thirdName"));
	    
	    final TableColumn<UserModel, String> userInitialsColumn = new TableColumn<>("ФИО");
	    userInitialsColumn.setCellValueFactory(new PropertyValueFactory<>("initials"));
	    
	    final TableColumn<UserModel, String> userPhoneColumn = new TableColumn<>("Телефон");
	    userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
	    
	    final TableColumn<UserModel, String> userPostColumn = new TableColumn<>("Должность");
	    userPostColumn.setCellValueFactory(new PropertyValueFactory<>("post"));
	    
	    final TableColumn<UserModel, Date> userBirthdayColumn = new TableColumn<>("Дата рождения");
	    userBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
	    userBirthdayColumn.setCellFactory(film -> {
	    	 return new TableCell<UserModel, Date>() {
	    	        @Override
	    	        protected void updateItem(Date item, boolean empty) {
	    	        	Platform.runLater(() -> {
		    	        	setText("");
		    	        	super.updateItem(item, empty);
		    	            if (item == null || empty) {
		    	                setText("");
		    	            } else {		    	            	
		    	            	final SimpleStringProperty property = new SimpleStringProperty();
		    	    	    	final DateFormat dateFormat = new SimpleDateFormat("dd MMMMM", myDateFormatSymbols);
		    	    	    	property.setValue(dateFormat.format(item));
		    	    	    	
		    	                setText(property.getValue());	
		    	            }
	    	        	});
	    	        }
	    	    };
    	});
	    
	    final TableColumn<UserModel, String> userDepartamentColumn = new TableColumn<>("Цех/Отдел");
	    userDepartamentColumn.setCellValueFactory(new PropertyValueFactory<>("departament"));
	    
	    userInitialsColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userPostColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userIconColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userFirstNameColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userSecondNameColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userThirdNameColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userBirthdayColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userPhoneColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userDepartamentColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userOrganizationColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    
		this.userBirthdayTable.getColumns().addAll(userOrganizationColumn, userIconColumn, 
				userInitialsColumn,
				userPostColumn,
				userDepartamentColumn,
				userBirthdayColumn);
		
		this.titleLabel.setText("Сегодня поздравим с днем рождения!");
		this.titleLabel.setStyle("-fx-font-size: 20pt;");
		this.titleLabel.setAlignment(Pos.CENTER);
		this.titleLabel.setContentDisplay(ContentDisplay.CENTER);
		this.titleLabel.setMaxWidth(Double.MAX_VALUE);
		this.tablePane.setTop(this.titleLabel);
		this.tablePane.setCenter(this.userBirthdayTable);
	}
	
	@Override
	public Pane getViewPane() {
		return this.tablePane;
	}

	@Override
	public void setData(final List<UserModel> userList) {
		Platform.runLater(() -> {
			userBirthdayTable.getItems().setAll(userList);
		});
	}

	@Override
	public void setTitle(final String text) {
		Platform.runLater(() -> {
			this.titleLabel.setText(text);
		});
	}

	@Override
	public void addData(UserModel user) {
		Platform.runLater(() -> {
			userBirthdayTable.getItems().add(user);
		});
	}

	@Override
	public void setMouseClick(EventHandler<? super MouseEvent> value) {
		this.userBirthdayTable.setOnMouseClicked(value);
	}

	@Override
	public UserModel getSelectModel() {
		return this.userBirthdayTable.getSelectionModel().getSelectedItem();
	}

	@Override
	public void clear() {
		Platform.runLater(() -> {
			tableModel.clear();
		});
	}

	@Override
	public String getTitle() {
		return titleLabel.getText();
	}
}


