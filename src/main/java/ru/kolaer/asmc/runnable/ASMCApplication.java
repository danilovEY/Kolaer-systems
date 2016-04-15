package ru.kolaer.asmc.runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.controller.CMainFrame;
import ru.kolaer.api.plugin.UniformSystemApplication;
import ru.kolaer.api.system.UniformSystemEditorKit;

/**
 * Реализация приложения для единой системы КолАЭР.
 * 
 * @author danilovey
 * @version 0.1
 */
public class ASMCApplication implements UniformSystemApplication {
	
	/**Панель с контентом плагина.*/
	private final BorderPane root = new BorderPane();
	/**Панель с .fxml-контента главного окна.*/
	private AnchorPane pane;
	private final UniformSystemEditorKit editorKid;
	public ASMCApplication(final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
	}

	@Override
	public String getIcon() {
		return "resources/aerIcon.png";
	}

	@Override
	public Pane getContent() {
		return this.root;
	}

	@Override
	public String getName() {
		return "АСУП";
	}

	@Override
	public void stop() {

	}

	@Override
	public void start() throws Exception {
		if(this.pane == null){
			SettingSingleton.initialization();
			try(final InputStream stream = Resources.V_MAIN_FRAME.openStream()){
				final FXMLLoader loader = new FXMLLoader();
				loader.setController(new CMainFrame());
				pane = loader.load(stream);
				((CMainFrame)loader.getController()).addEditorKit(this.editorKid);
				final InputStream inputStream = this.getClass().getResourceAsStream("/resources/CSS/Default/Default.css");
				final URL inputStreamUrl = this.getClass().getResource("/resources/CSS/Default/Default.css");
				if(inputStreamUrl != null)
					pane.getStylesheets().addAll(inputStreamUrl.toExternalForm());
				inputStream.close();
				Platform.runLater(() -> {
					root.setCenter(pane);
					root.setPrefSize(800, 600);	
				});
			}catch(final MalformedURLException e){
				throw e;
			}catch(final IOException e){
				throw e;
			}
		}
	}
}
