package ru.kolaer.birthday.runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMCalendarAffiliates;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMCalendarKAER;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMTableWithUsersBithdayObserverImpl;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

public class BirthdayApplication implements UniformSystemApplication {
	private final Logger LOG = LoggerFactory.getLogger(BirthdayApplication.class);

	private final UniformSystemEditorKit editorKid;
	private final BorderPane root = new BorderPane();
	private AnchorPane mainPane;
	private VMTableWithUsersBirthdayObserver vmTable;

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
			this.vmTable = new VMTableWithUsersBithdayObserverImpl(this.editorKid);
			frameContent.setVMTableWithUsersBirthday(this.vmTable);
		}, service).exceptionally(t -> {
			LOG.error("Ошибка!", t);
			return null;
		}).thenRunAsync(() -> {
			CompletableFuture<VMCalendar> cKaer = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarKAER = new VMCalendarKAER(this.editorKid);
				calendarKAER.registerObserver(this.vmTable);
				return calendarKAER;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cCo = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarCO = new VMCalendarAffiliates("Центральный аппарат", this.editorKid);
				calendarCO.registerObserver(this.vmTable);
				return calendarCO;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cBal = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarBal = new VMCalendarAffiliates("БалаковоАтомэнергоремонт", this.editorKid);
				calendarBal.registerObserver(this.vmTable);
				return calendarBal;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cVol = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarVol = new VMCalendarAffiliates("ВолгодонскАтомэнергоремонт", this.editorKid);
				calendarVol.registerObserver(this.vmTable);
				return calendarVol;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cCal = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarCal = new VMCalendarAffiliates("КалининАтомэнергоремонт", this.editorKid);
				calendarCal.registerObserver(this.vmTable);
				return calendarCal;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cKur = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarKur = new VMCalendarAffiliates("КурскАтомэнергоремонт", this.editorKid);
				calendarKur.registerObserver(this.vmTable);
				return calendarKur;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cLen = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarLen = new VMCalendarAffiliates("ЛенАтомэнергоремонт", this.editorKid);
				calendarLen.registerObserver(this.vmTable);
				return calendarLen;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cNov = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarNov = new VMCalendarAffiliates("НововоронежАтомэнергоремонт", this.editorKid);
				calendarNov.registerObserver(this.vmTable);
				return calendarNov;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cSmo = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarSmo = new VMCalendarAffiliates("СмоленскАтомэнергоремонт", this.editorKid);
				calendarSmo.registerObserver(this.vmTable);
				return calendarSmo;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});

			CompletableFuture<VMCalendar> cUra = CompletableFuture.supplyAsync(() -> {
				final VMCalendar calendarUra = new VMCalendarAffiliates("УралАтомэнергоремонт", this.editorKid);
				calendarUra.registerObserver(this.vmTable);
				return calendarUra;
			}, service).exceptionally(t -> {
				LOG.error("Ошибка!", t);
				return null;
			});
			
			try{
				frameContent.addVMCalendar(cKaer.get());
				frameContent.addVMCalendar(cCo.get());
				frameContent.addVMCalendar(cBal.get());
				frameContent.addVMCalendar(cVol.get());
				frameContent.addVMCalendar(cCal.get());
				frameContent.addVMCalendar(cKur.get());
				frameContent.addVMCalendar(cLen.get());
				frameContent.addVMCalendar(cNov.get());
				frameContent.addVMCalendar(cSmo.get());
				frameContent.addVMCalendar(cUra.get());
			}catch(InterruptedException | ExecutionException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			service.shutdown();
		});

		


		
		
	}

}
