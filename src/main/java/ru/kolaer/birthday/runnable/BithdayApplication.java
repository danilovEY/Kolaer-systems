package ru.kolaer.birthday.runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.birthday.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

public class BithdayApplication implements UniformSystemApplication {
	private final UniformSystemEditorKit editorKid;
	private final BorderPane root = new BorderPane();
	private AnchorPane mainPane;
	
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
				FXMLLoader loader = new FXMLLoader();
				this.mainPane = loader.load(stream);
				Platform.runLater(() -> {
					root.setCenter(this.mainPane);
					root.setPrefSize(800, 600);
					editorKid.getUISystemUS().getNotification().showSimpleNotify("Init", "Bithday!");
				});
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

}
