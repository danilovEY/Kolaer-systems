package ru.kolaer.birthday.runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthdayObserver;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMCalendarImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMTableWithUsersBithdayObserverImpl;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

public class BirthdayApplication implements UniformSystemApplication {
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
		if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.NOT_AVAILABLE) {
			this.editorKid.getUISystemUS().getDialog().showErrorDialog("Ошибка!", "Сервер не доступен! Проверьте подключение к локальной сети.");
			return;
		}
		
		if(this.mainPane == null) {
			try(final InputStream stream = VMMainFrameImpl.FXML_VIEW.openStream()){
				final FXMLLoader loader = new FXMLLoader();
				this.mainPane = loader.load(stream);
				final VMMainFrameImpl frame =  loader.getController();
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
		this.vmTable = new VMTableWithUsersBithdayObserverImpl(this.editorKid);		
		frameContent.setVMTableWithUsersBirthday(this.vmTable);
		
		final VMCalendar calendar = new VMCalendarImpl(this.editorKid);
		calendar.registerObserver(vmTable);
		
		frameContent.addVMCalendar(calendar);
		
	}

}
