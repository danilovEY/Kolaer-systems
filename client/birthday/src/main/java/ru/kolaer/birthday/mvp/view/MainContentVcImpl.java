package ru.kolaer.birthday.mvp.view;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * Реализация {@linkplain MainContentVc}.
 *
 * @author danilovey
 * @version 0.1
 */
public class MainContentVcImpl implements MainContentVc {
	private BorderPane calendarPane;
	private BorderPane tablePane;
	private VBox paneWithCalendars;
	private TableWithUsersBirthdayVcImpl tableWithUsersBirthdayVc;

	private void setTableWithUsersBirthdayVc(TableWithUsersBirthdayVc vmTable) {
		Button butTodayUsers = new Button("Показать сегодняшние дни рождения!");
		butTodayUsers.setMaxWidth(Double.MAX_VALUE);
		butTodayUsers.setOnAction(e -> vmTable.showTodayBirthday());
		paneWithCalendars.getChildren().add(butTodayUsers);

		tablePane.setCenter(vmTable.getContent());
		vmTable.showTodayBirthday();
	}

	@Override
	public void addVMCalendar(CalendarVc calendar) {
		calendar.registerObserver(tableWithUsersBirthdayVc);

		Button calendarBut = new Button(calendar.getTitle());
		calendarBut.setMaxWidth(Double.MAX_VALUE);
		calendarBut.setOnAction(e -> {
			if(calendar.getContent() == null) {
				calendar.initView(initCalendar -> calendarPane.setCenter(initCalendar.getContent()));
			} else {
				calendarPane.setCenter(calendar.getContent());
			}
		});

		this.paneWithCalendars.getChildren().add(calendarBut);
	}

	@Override
	public void initView(Consumer<MainContentVc> viewVisit) {
		tablePane = new BorderPane();
		calendarPane = new BorderPane();
		paneWithCalendars = new VBox();

		tableWithUsersBirthdayVc = new TableWithUsersBirthdayVcImpl();
		tableWithUsersBirthdayVc.initView(this::setTableWithUsersBirthdayVc);

		addVMCalendar(new CalendarVcKAER());
		addVMCalendar(new CalendarVcAffiliates("Центральный аппарат"));
		addVMCalendar(new CalendarVcAffiliates("БалаковоАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("ВолгодонскАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("КалининАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("КурскАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("ЛенАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("НововоронежАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("СмоленскАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAffiliates("УралАтомэнергоремонт"));
		addVMCalendar(new CalendarVcAll());

		paneWithCalendars.getChildren().add(calendarPane);

		tablePane.setRight(paneWithCalendars);

		viewVisit.accept(this);
	}

	@Override
	public Parent getContent() {
		return tablePane;
	}
}