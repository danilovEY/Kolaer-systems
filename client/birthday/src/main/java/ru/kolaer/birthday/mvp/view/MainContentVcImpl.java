package ru.kolaer.birthday.mvp.view;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.birthday.mvp.presenter.CalendarVc;
import ru.kolaer.birthday.mvp.presenter.impl.CalendarVcAffiliates;
import ru.kolaer.birthday.mvp.presenter.impl.CalendarVcAll;
import ru.kolaer.birthday.mvp.presenter.impl.CalendarVcKAER;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Реализация {@linkplain MainContentVc}.
 *
 * @author danilovey
 * @version 0.1
 */
public class MainContentVcImpl implements MainContentVc {
	private final Logger log = LoggerFactory.getLogger(MainContentVcImpl.class);
	/**Presenter таблици.*/
	private TableWithUsersBirthdayVc vmTable;
	private List<CalendarVc> calendarList = new ArrayList<>();

	private BorderPane calendarPane;
	
	private BorderPane tablePane;
	private VBox paneWithCalendars;

	@Override
	public void setVMTableWithUsersBirthday(PTableWithUsersBirthdayObserver vmTable) {
		this.vmTable = vmTable;
		final TableWithUsersBirthdayVc vTable = vmTable.getView();
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
	public void setVMTableWithUsersBirthday(TableWithUsersBirthdayVc vmTable) {

	}

	@Override
	public void addVMCalendar(final CalendarVc calendar) {
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

	@Override
	public void initView(Consumer<MainContentVc> viewVisit) {
		calendarPane = new BorderPane();
		paneWithCalendars.getChildren().add(this.calendarPane);

		ExecutorService service = Executors.newCachedThreadPool();
		CompletableFuture.runAsync(() -> {
			this.vmTable = new TableWithUsersBirthdayVcImpl(this.editorKid);
			frameContent.setVMTableWithUsersBirthday(this.vmTable);
		}, service).exceptionally(t -> {
			log.error("Ошибка!", t);
			return null;
		}).thenRunAsync(() -> {
			CompletableFuture<CalendarVc> cKaer = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarKAER = new CalendarVcKAER(this.editorKid);
				calendarKAER.registerObserver(this.vmTable);
				return calendarKAER;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cCo = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarCO = new CalendarVcAffiliates("Центральный аппарат");
				calendarCO.registerObserver(this.vmTable);
				return calendarCO;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cBal = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarBal = new CalendarVcAffiliates("БалаковоАтомэнергоремонт");
				calendarBal.registerObserver(this.vmTable);
				return calendarBal;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cVol = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarVol = new CalendarVcAffiliates("ВолгодонскАтомэнергоремонт");
				calendarVol.registerObserver(this.vmTable);
				return calendarVol;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cCal = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarCal = new CalendarVcAffiliates("КалининАтомэнергоремонт");
				calendarCal.registerObserver(this.vmTable);
				return calendarCal;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cKur = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarKur = new CalendarVcAffiliates("КурскАтомэнергоремонт");
				calendarKur.registerObserver(this.vmTable);
				return calendarKur;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cLen = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarLen = new CalendarVcAffiliates("ЛенАтомэнергоремонт");
				calendarLen.registerObserver(this.vmTable);
				return calendarLen;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cNov = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarNov = new CalendarVcAffiliates("НововоронежАтомэнергоремонт");
				calendarNov.registerObserver(this.vmTable);
				return calendarNov;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cSmo = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarSmo = new CalendarVcAffiliates("СмоленскАтомэнергоремонт");
				calendarSmo.registerObserver(this.vmTable);
				return calendarSmo;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cUra = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarUra = new CalendarVcAffiliates("УралАтомэнергоремонт");
				calendarUra.registerObserver(this.vmTable);
				return calendarUra;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<CalendarVc> cAll = CompletableFuture.supplyAsync(() -> {
				final CalendarVc calendarAll = new CalendarVcAll();
				calendarAll.registerObserver(this.vmTable);
				return calendarAll;
			}, service).exceptionally(t -> {
				log.error("Ошибка!", t);
				return null;
			});

			try{
				frameContent.addVMCalendar(cKaer.get());
				CompletableFuture.runAsync(() -> {
					try{
						frameContent.addVMCalendar(cCo.get());
						frameContent.addVMCalendar(cBal.get());
						frameContent.addVMCalendar(cVol.get());
						frameContent.addVMCalendar(cCal.get());
						frameContent.addVMCalendar(cKur.get());
						frameContent.addVMCalendar(cLen.get());
						frameContent.addVMCalendar(cNov.get());
						frameContent.addVMCalendar(cSmo.get());
						frameContent.addVMCalendar(cUra.get());
						frameContent.addVMCalendar(cAll.get());
					} catch(final Exception e){
						log.error("Ошибка при добавлении календаря!", e);
					}
				});
			} catch(final Exception ex){
				log.error("Ошибка при добавлении календаря KolAER!", ex);
			}
			service.shutdown();
		}, service);
	}

	@Override
	public Parent getContent() {
		return m;
	}
}
