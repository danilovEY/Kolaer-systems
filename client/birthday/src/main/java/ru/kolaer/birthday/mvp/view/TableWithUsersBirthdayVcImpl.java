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
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.model.impl.UserModelImpl;
import ru.kolaer.birthday.tools.BirthdayTools;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
		userBirthdayTable.setPlaceholder(new Label("Именинники отсутствуют!"));
		userBirthdayTable.setEditable(true);

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
						System.out.println(item);
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

		VSearchUsers searchUsers = new VSearchUsersImpl();
		searchUsers.addObserver(this);
		searchUsers.initView(initSearch -> titlePane.setRight(initSearch.getContent()));

		this.mainPane.setTop(this.titlePane);
		this.mainPane.setCenter(this.userBirthdayTable);
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
	public void updateSelectedDate(String title, LocalDate date, List<UserModel> users) {
		Tools.runOnWithOutThreadFX(() -> {
			userBirthdayTable.getItems().clear();
			updateUsers(users);

			if(users.size() > 0) {
				titleLabel.setText("\"" + date.format(BirthdayTools.getDateTimeFormatter()) + "\" день рождения у:");
			} else {
				titleLabel.setText("\"" + date.format(BirthdayTools.getDateTimeFormatter()) + "\" именинники отсутствуют!");
			}
		});
	}

	@Override
	public void showTodayBirthday() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			Date now = new Date();

			List<UserModel> userModels = new ArrayList<>();

			ServerResponse<List<EmployeeDto>> usersDataAll = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getGeneralEmployeesTable()
					.getUsersByBirthday(now);

			if(!usersDataAll.isServerError()) {
				List<UserModel> users = usersDataAll
						.getResponse()
						.stream()
						.map(UserModelImpl::new)
						.collect(Collectors.toList());

				userModels.addAll(users);
			} else {
				UniformSystemEditorKitSingleton.getInstance()
						.getUISystemUS()
						.getNotification()
						.showErrorNotify(usersDataAll.getExceptionMessage());
			}

			ServerResponse<List<EmployeeOtherOrganizationDto>> usersOther = UniformSystemEditorKitSingleton.getInstance()
					.getUSNetwork()
					.getKolaerWebServer()
					.getApplicationDataBase()
					.getEmployeeOtherOrganizationTable()
					.getUsersBirthdayToday();
			if(!usersOther.isServerError()) {
				List<UserModel> users = usersOther
						.getResponse()
						.stream()
						.map(UserModelImpl::new)
						.collect(Collectors.toList());

				userModels.addAll(users);
			} else {
				UniformSystemEditorKitSingleton.getInstance()
						.getUISystemUS()
						.getNotification()
						.showErrorNotify(usersDataAll.getExceptionMessage());
			}

			Tools.runOnWithOutThreadFX(() -> {
				titleLabel.setText("Сегодня день рождения у:");
				updateUsers(userModels);
			});
		});
		executorService.shutdown();
	}

	@Override
	public void updateUsers(List<UserModel> users) {
		Tools.runOnWithOutThreadFX(() -> {
			if(users == null || users.isEmpty()) {
				userBirthdayTable.setPlaceholder(new Label("Именинники отсутствуют!"));
			} else {
				userBirthdayTable.getItems().setAll(users);
			}
		});
	}
}


