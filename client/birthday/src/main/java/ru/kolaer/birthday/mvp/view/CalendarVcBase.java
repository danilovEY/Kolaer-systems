package ru.kolaer.birthday.mvp.view;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import ru.kolaer.birthday.mvp.presenter.ObserverCalendar;
import ru.kolaer.common.tools.Tools;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Реализация базовых методов для presenter'ов календарей.
 *
 * @author danilovey
 * @version 0.1
 */
public abstract class CalendarVcBase implements CalendarVc {
	/**Имя организации.*/
	protected final String organization;
	/**Слушатель календаря.*/
	protected ObserverCalendar observerCalendar;
	/**Панель с календарем.*/
	protected BorderPane mainPane;
	protected DatePicker pick;
	/**Заголовок календаря.*/
	protected Label title;

	public CalendarVcBase(String organization) {
		this.organization = organization;
	}
	@Override
	public void registerObserver(ObserverCalendar observer) {
		this.observerCalendar = observer;
	}

	@Override
	public void removeObserver(ObserverCalendar observer) {
		this.observerCalendar = null;
	}

	@Override
	public void setDayCellFactory(CustomCallback value) {
		Tools.runOnWithOutThreadFX(() -> {
			pick.setDayCellFactory(value);
			mainPane.setCenter(new DatePickerSkin(pick).getPopupContent());
		});
	}

	@Override
	public void initView(Consumer<CalendarVc> viewVisit) {
		mainPane = new BorderPane();
		pick = new DatePicker();
		pick.setShowWeekNumbers(true);
		pick.setOnAction(e -> {
			if(observerCalendar != null) {
				notifySelectedDate(pick.getValue());
			}
		});

		title = new Label(organization);
		title.setAlignment(Pos.CENTER);
		title.setStyle("-fx-font-size: 12pt;");
		title.setMaxWidth(Double.MAX_VALUE);

		mainPane.setTop(title);

		viewVisit.accept(this);
	}

	@Override
	public Parent getContent() {
		return mainPane;
	}

	@Override
	public String getTitle() {
		return organization;
	}

	public abstract void notifySelectedDate(LocalDate date);
}
