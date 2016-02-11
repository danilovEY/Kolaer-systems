package ru.kolaer.birthday.runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.presenter.impl.PCalendarAffiliates;
import ru.kolaer.birthday.mvp.presenter.impl.PCalendarKAER;
import ru.kolaer.birthday.mvp.presenter.impl.PTableWithUsersBithdayObserverImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

/**
 * Реализация контента модуля.
 *
 * @author danilovey
 * @version 0.1
 */
public class BirthdayApplication implements UniformSystemApplication {
	private final Logger LOG = LoggerFactory.getLogger(BirthdayApplication.class);

	private final UniformSystemEditorKit editorKid;
	private final BorderPane root = new BorderPane();
	private AnchorPane mainPane;
	private PTableWithUsersBirthdayObserver vmTable;

	public BirthdayApplication(UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public Pane getContent() {
		return this.root;
	}

	@Override
	public String getName() {
		return "Дни рождения";
	}

	@Override
	public void run() throws Exception {
		if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.NOT_AVAILABLE){
			this.editorKid.getUISystemUS().getDialog().showErrorDialog("Ошибка!", "Сервер не доступен! Проверьте подключение к локальной сети.");
			return;
		}

		if(this.mainPane == null){
			try(final InputStream stream = this.getClass().getResourceAsStream("/birthdayView/VMainFrame.fxml")){
				final FXMLLoader loader = new FXMLLoader();
				this.mainPane = loader.load(stream);
				final VMMainFrameImpl frame = loader.getController();
				Platform.runLater(() -> {
					root.setCenter(this.mainPane);
					root.setPrefSize(800, 600);
				});
				this.initTable(frame);
			}catch(MalformedURLException e){
				throw e;
			}catch(IOException e){
				throw e;
			}
		}
	}

	@Override
	public void stop() throws Exception {

	}

	private void initTable(VMMainFrame frameContent) {
		ExecutorService service = Executors.newCachedThreadPool();
		CompletableFuture.runAsync(() -> {
			this.vmTable = new PTableWithUsersBithdayObserverImpl(this.editorKid);
			frameContent.setVMTableWithUsersBirthday(this.vmTable);
		}, service).exceptionally(t -> {
			LOG.error("Ошибка!", t);
			return null;
		}).thenRunAsync(() -> {
			CompletableFuture<PCalendar> cKaer = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarKAER = new PCalendarKAER(this.editorKid);
				calendarKAER.registerObserver(this.vmTable);
				return calendarKAER;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cCo = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarCO = new PCalendarAffiliates("Центральный аппарат", this.editorKid);
				calendarCO.registerObserver(this.vmTable);
				return calendarCO;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cBal = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarBal = new PCalendarAffiliates("БалаковоАтомэнергоремонт", this.editorKid);
				calendarBal.registerObserver(this.vmTable);
				return calendarBal;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cVol = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarVol = new PCalendarAffiliates("ВолгодонскАтомэнергоремонт", this.editorKid);
				calendarVol.registerObserver(this.vmTable);
				return calendarVol;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cCal = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarCal = new PCalendarAffiliates("КалининАтомэнергоремонт", this.editorKid);
				calendarCal.registerObserver(this.vmTable);
				return calendarCal;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cKur = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarKur = new PCalendarAffiliates("КурскАтомэнергоремонт", this.editorKid);
				calendarKur.registerObserver(this.vmTable);
				return calendarKur;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cLen = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarLen = new PCalendarAffiliates("ЛенАтомэнергоремонт", this.editorKid);
				calendarLen.registerObserver(this.vmTable);
				return calendarLen;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cNov = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarNov = new PCalendarAffiliates("НововоронежАтомэнергоремонт", this.editorKid);
				calendarNov.registerObserver(this.vmTable);
				return calendarNov;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cSmo = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarSmo = new PCalendarAffiliates("СмоленскАтомэнергоремонт", this.editorKid);
				calendarSmo.registerObserver(this.vmTable);
				return calendarSmo;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<PCalendar> cUra = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarUra = new PCalendarAffiliates("УралАтомэнергоремонт", this.editorKid);
				calendarUra.registerObserver(this.vmTable);
				return calendarUra;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
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
					} catch(final Exception e){
						LOG.error("Ошибка при добавлении календаря!", e);
					}
				});
			} catch(final Exception ex){
				LOG.error("Ошибка при добавлении календаря KolAER!", ex);
			}			
			service.shutdown();
		}, service);
	}
}