package ru.kolaer.birthday.runnable;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.ServerStatus;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.birthday.mvp.presenter.PTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.presenter.impl.PCalendarAffiliates;
import ru.kolaer.birthday.mvp.presenter.impl.PCalendarAll;
import ru.kolaer.birthday.mvp.presenter.impl.PCalendarKAER;
import ru.kolaer.birthday.mvp.presenter.impl.PTableWithUsersBirthdayObserverImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.birthday.service.BirthdayOnHoliday;
import ru.kolaer.birthday.service.BirthdayService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Реализация модуля.
 *
 * @author danilovey
 * @version 0.1
 */
public class BirthdayPlugin implements UniformSystemPlugin {
	private final static Logger LOG = LoggerFactory.getLogger(BirthdayPlugin.class);

	private UniformSystemEditorKit editorKid;
	private BorderPane root;
	private AnchorPane mainPane;
	private PTableWithUsersBirthdayObserver vmTable;
	/**Список служб.*/
	private List<Service> servicesList;
	
	@Override
	public void initialization(final UniformSystemEditorKit editorKid) throws Exception {
		this.editorKid = editorKid;
		this.root = new BorderPane();
		this.servicesList = new ArrayList<>();
		this.servicesList.add(new BirthdayService(editorKid));
		this.servicesList.add(new BirthdayOnHoliday(editorKid));
	}

	@Override
	public URL getIcon() {
		return null;
	}

	@Override
	public void start() throws Exception {
		if(this.mainPane == null){
			try {
				final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/birthdayView/VMainFrame.fxml"));
				final VMMainFrameImpl frame = new VMMainFrameImpl();
				loader.setController(frame);
				this.mainPane = loader.load();
				Platform.runLater(() -> {
					root.setCenter(this.mainPane);
					root.setPrefSize(800, 600);
				});
				this.initTable(frame);
			} catch(IOException e){
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
			this.vmTable = new PTableWithUsersBirthdayObserverImpl(this.editorKid);
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

			CompletableFuture<PCalendar> cAll = CompletableFuture.supplyAsync(() -> {
				final PCalendar calendarAll = new PCalendarAll(this.editorKid);
				calendarAll.registerObserver(this.vmTable);
				return calendarAll;
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
						frameContent.addVMCalendar(cAll.get());
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

	@Override
	public List<Service> getServices() {
		return this.servicesList;
	}

	@Override
	public void updatePluginObjects(String key, Object object) {
	}

	@Override
	public void setContent(Parent parent) {

	}

	@Override
	public Parent getContent() {
		return root;
	}
}
