package ru.kolaer.birthday.mvp.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.tools.BirthdayTools;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * View - таблици с днями рождениями.
 *
 * @author danilovey
 * @version 0.1
 */
public class TableWithUsersBirthdayVcImpl implements TableWithUsersBirthdayVc {
	private Logger log = LoggerFactory.getLogger(TableWithUsersBirthdayVcImpl.class);

	/**Панель с таблицой.*/
	private BorderPane mainPane;
	private BorderPane titlePane;
	/**Таблица*/
	private TableView<UserModel> userBirthdayTable;
	/**Заголовок таблици.*/
	private Label titleLabel;
	
	private void init() {
		mainPane = new BorderPane();
		titlePane = new BorderPane();
		titleLabel = new Label();

		userBirthdayTable = new TableView<>();
		userBirthdayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		userBirthdayTable.getSelectionModel().setCellSelectionEnabled(true);
		userBirthdayTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		userBirthdayTable.setEditable(true);
		
		
		MenuItem item = new MenuItem("Копировать");

		userBirthdayTable.setContextMenu(new ContextMenu(item));


	    item.setOnAction(event -> {
            /*StringBuilder clipboardString = new StringBuilder();
            Iterator<Integer> iter = userBirthdayTable.getSelectionModel().getSelectedIndices().iterator();
            while(iter.hasNext()) {
            	int index = iter.next();
	            for(TablePosition<UserModel, Object> col : userBirthdayTable.getSelectionModel().getSelectedCells()){
	            	if(col.getRow() == index) {
	            		ObservableValue<Object> observableValue = col.getTableColumn().getCellObservableValue(index);
		            	if(observableValue != null) {
		            		if(observableValue.getValue().getClass() == Date.class) {
		            			SimpleStringProperty property = new SimpleStringProperty();
		    	    	    	DateFormat dateFormat = new SimpleDateFormat("dd MMMMM", myDateFormatSymbols);
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
            ClipboardContent content = new ClipboardContent();
            //В буфер
            content.putString(clipboardString.toString());
            Clipboard.getSystemClipboard().setContent(content);*/
	    });

		userBirthdayTable.setOnMouseClicked(event -> {
			if(event.getClickCount() == 2) {
				DetailedInformationVc.show(userBirthdayTable.getSelectionModel().getSelectedItem());
			}
		});

		
		TableColumn<UserModel, String> userIconColumn = new TableColumn<>("Фотография");
	    userIconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
	    userIconColumn.setCellFactory((TableColumn<UserModel, String> param) -> new TableCell<UserModel, String>(){
            @Override
            public void updateItem(String item, boolean empty) {
				ImageView imageView;

            	if(getGraphic() == null) {
					imageView = new ImageView();
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(100);
					imageView.setFitWidth(100);

					setGraphic(imageView);
				} else {
            		imageView = (ImageView) getGraphic();
				}

				if(!empty) {
					if(item == null || item.equals("")){
						imageView.setImage(new Image(getClass().getResource("/nonePicture.jpg").toString(), false));
					} else {
						imageView.setImage(new Image(item, false));
					}
					this.setGraphic(imageView);
				} else {
            		imageView.setImage(null);
				}
            }
        });
	    
	    TableColumn<UserModel, Integer> userOrganizationColumn = new TableColumn<>("Организация");
	    userOrganizationColumn.setCellValueFactory(new PropertyValueFactory<>("organization"));
	    
	    TableColumn<UserModel, String> userFirstNameColumn = new TableColumn<>("Имя");
	    userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    
	    TableColumn<UserModel, String> userSecondNameColumn = new TableColumn<>("Фамилия");
	    userSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
	    
	    TableColumn<UserModel, String> userThirdNameColumn = new TableColumn<>("Отчество");
	    userThirdNameColumn.setCellValueFactory(new PropertyValueFactory<>("thirdName"));
	    
	    TableColumn<UserModel, String> userInitialsColumn = new TableColumn<>("ФИО");
	    userInitialsColumn.setCellValueFactory(new PropertyValueFactory<>("initials"));
	    
	    TableColumn<UserModel, String> userPhoneColumn = new TableColumn<>("Телефон");
	    userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("homePhoneNumber"));
	    
	    TableColumn<UserModel, String> userPostColumn = new TableColumn<>("Должность");
	    userPostColumn.setCellValueFactory(new PropertyValueFactory<>("post"));
	    
	    TableColumn<UserModel, Date> userBirthdayColumn = new TableColumn<>("Дата рождения");
	    userBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
	    userBirthdayColumn.setCellFactory(film -> new TableCell<UserModel, Date>() {
               @Override
               protected void updateItem(Date item, boolean empty) {
				   setText("");
				   super.updateItem(item, empty);
				   if (item == null || empty) {
					   setText("");
				   } else {
					   setText(BirthdayTools.dateToStringWithOutYear(item));
				   }
               }
           });
	    
	    TableColumn<UserModel, String> userDepartmentColumn = new TableColumn<>("Цех/Отдел");
	    userDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
	    
	    userInitialsColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userPostColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userIconColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userFirstNameColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userSecondNameColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userThirdNameColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userBirthdayColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userPhoneColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userDepartmentColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    userOrganizationColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
	    
		userBirthdayTable.getColumns().addAll(userOrganizationColumn, userIconColumn,
				userInitialsColumn,
				userPostColumn,
				userDepartmentColumn,
				userBirthdayColumn);
		
		this.titleLabel.setText("Сегодня поздравим с днем рождения!");
		this.titleLabel.setStyle("-fx-font-size: 20pt;");
		this.titleLabel.setAlignment(Pos.CENTER);
		this.titleLabel.setContentDisplay(ContentDisplay.CENTER);
		this.titleLabel.setMaxWidth(Double.MAX_VALUE);

		this.titlePane.setCenter(this.titleLabel);
		this.titlePane.setRight(searchUsers.getContent());

		this.mainPane.setTop(this.titlePane);
		this.mainPane.setCenter(this.userBirthdayTable);
	}

	@Override
	public void setData(List<UserModel> userList) {
		Tools.runOnWithOutThreadFX(() -> {
			if(userList == null || userList.isEmpty()) {
				userBirthdayTable.setPlaceholder(new Label("---"));
			} else {
				userBirthdayTable.getItems().setAll(userList);
			}
		});
	}

	@Override
	public void setTitle(String text) {
		Tools.runOnWithOutThreadFX(() -> this.titleLabel.setText(text));
	}

	@Override
	public void clear() {

	}

	@Override
	public void initView(Consumer<TableWithUsersBirthdayVc> viewVisit) {
		init();

		viewVisit.accept(this);
	}

	@Override
	public Parent getContent() {
		return mainPane;
	}









	@Override
	public void updateSelectedDate(LocalDate date, List<UserModel> users) {
		if(users.size() > 0) {
			this.userBirthdayTable.setData(users);
			this.table.setTitle("\"" + date.format(formatter) + "\" день рождения у:");
		} else {
			this.table.clear();
			this.table.setTitle("\"" + date.format(formatter) + "\" именинники отсутствуют!");
			this.table.setNoContentText("\"" + date.format(formatter) + "\" именинники отсутствуют!");
		}
	}


	@Override
	public void showTodayBirthday() {
		CompletableFuture.runAsync(() -> {
			final ServerResponse<List<EmployeeDto>> usersKolaer = editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getGeneralEmployeesTable().getUsersBirthdayToday();
			for(final EmployeeDto user : usersKolaer.getResponse()) {
				final UserModel userModel = new UserModelImpl(user);
				table.addData(userModel);
			}

			final ServerResponse<List<EmployeeOtherOrganizationDto>> usersOther = editorKid.getUSNetwork().getKolaerWebServer().getApplicationDataBase().getEmployeeOtherOrganizationTable().getUsersBirthdayToday();
			for(final EmployeeOtherOrganizationDto user : usersOther.getResponse()) {
				final UserModel userModel = new UserModelImpl(user);
				userModel.setOrganization(user.getOrganization());
				table.addData(userModel);
			}
		});
	}

	@Override
	public void updateUsers(List<UserModel> users) {
		this.table.setData(users);
	}
}


