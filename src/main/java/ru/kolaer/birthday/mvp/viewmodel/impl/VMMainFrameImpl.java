package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;

/**
 * Реализация {@linkplain VMMainFrame}.
 *
 * @author danilovey
 * @version 0.1
 */
public class VMMainFrameImpl extends Application implements VMMainFrame {
	/**Путь к view.*/
	public static final URL FXML_VIEW = VMMainFrameImpl.class.getResource("/resources/birthdayView/VMainFrame.fxml");
	private final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	/**Presenter таблици.*/
	private PTableWithUsersBirthdayObserver vmTable;
	private List<PCalendar> calendarList = new ArrayList<>();

	private BorderPane calendarPane;
	
	@FXML
	private BorderPane tablePane;
	@FXML
	private VBox paneWithCalendars;
	
	@FXML
	public void initialize() {
		this.calendarPane = new BorderPane();
		this.paneWithCalendars.getChildren().add(this.calendarPane);
	}
	
	@Override
	public void start(final Stage primaryStage) throws Exception {	
		try {
			final Parent root = FXMLLoader.load(FXML_VIEW);
			primaryStage.setScene(new Scene(root, 800, 600));
		} catch(IOException ex) {
			LOG.error("Ошибка при чтении {}", FXML_VIEW, ex);
			
		}
		primaryStage.show();
	}

	@Override
	public PTableWithUsersBirthdayObserver getVMTableWithUsersBirthday() {
		return this.vmTable;
	}

	@Override
	public void setVMTableWithUsersBirthday(PTableWithUsersBirthdayObserver vmTable) {
		this.vmTable = vmTable;
		final VTableWithUsersBirthday vTable = vmTable.getView();
		Platform.runLater(() -> {
			final Button butTodayUsers = new Button("Показать сегодняшние дни рождения!");
			butTodayUsers.setMaxWidth(Double.MAX_VALUE);
			butTodayUsers.setOnAction(e -> {
				vmTable.showTodayBirthday();
			});
			this.paneWithCalendars.getChildren().add(0,butTodayUsers);
			this.tablePane.setCenter(vTable.getViewPane());
		});
	}

	@Override
	public void addVMCalendar(final PCalendar calendar) {
		Platform.runLater(() -> {
			this.calendarList.add(calendar);
			final Button calendarBut = new Button(calendar.getView().getTitle());
			calendarBut.setMaxWidth(Double.MAX_VALUE);
			calendarBut.setOnAction(e -> {
				if(!calendar.isInitDayCellFactory()) {
					calendar.initDayCellFactory();
				}
				this.calendarPane.setCenter(calendar.getView().getViewPane());
				
			});
			
			this.paneWithCalendars.getChildren().add(this.paneWithCalendars.getChildren().size() - 1, calendarBut);
		});
	}
}
