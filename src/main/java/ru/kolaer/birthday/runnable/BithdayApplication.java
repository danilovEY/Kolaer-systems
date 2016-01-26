package ru.kolaer.birthday.runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBithday;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMTableWithUsersBithdayImpl;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.system.ServerStatus;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

public class BithdayApplication implements UniformSystemApplication {
	private final UniformSystemEditorKit editorKid;
	private final BorderPane root = new BorderPane();
	private AnchorPane mainPane;
	private VMTableWithUsersBithday vmTable;
	
	public BithdayApplication(UniformSystemEditorKit editorKid) {
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
		if(this.mainPane == null) {
			try(final InputStream stream = VMMainFrameImpl.FXML_VIEW.openStream()){
				final FXMLLoader loader = new FXMLLoader();
				this.mainPane = loader.load(stream);
				final VMMainFrameImpl frame =  loader.getController();
				Platform.runLater(() -> {
					root.setCenter(this.mainPane);
					root.setPrefSize(800, 600);
				});
				
				if(this.editorKid.getUSNetwork().getServerStatus() == ServerStatus.NOT_AVAILABLE) {
					this.editorKid.getUISystemUS().getDialog().showErrorDialog("Ошибка!", "Сервер не доступен! Проверьте подключение к локальной сети.");
					return;
				}
				
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
		this.vmTable = new VMTableWithUsersBithdayImpl(this.editorKid);		
		frameContent.setVMTableWithUsersBithday(this.vmTable);
		
		
	}

}
