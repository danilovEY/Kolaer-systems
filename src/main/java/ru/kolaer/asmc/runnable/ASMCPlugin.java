package ru.kolaer.asmc.runnable;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.controller.CMainFrame;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Плагин для единой системы КолАЭР.
 * @author danilovey
 * @version 0.1
 */
public class ASMCPlugin implements UniformSystemPlugin {
	/**Панель с контентом плагина.*/
	private BorderPane root;
	/**Панель с .fxml-контента главного окна.*/
	private AnchorPane pane;
	private UniformSystemEditorKit editorKit;
	
	@Override
	public void initialization(final UniformSystemEditorKit editorKit) throws Exception {
		this.editorKit = editorKit;
		this.root = new BorderPane();
	}

	@Override
	public URL getIcon() {
		return this.getClass().getResource("/aerIcon.png");
	}

	@Override
	public void start() throws Exception {
		if(this.pane == null){
			SettingSingleton.initialization();
			try(final InputStream stream = Resources.V_MAIN_FRAME.openStream()){
				final FXMLLoader loader = new FXMLLoader();
				loader.setController(new CMainFrame());
				pane = loader.load(stream);
				((CMainFrame)loader.getController()).addEditorKit(this.editorKit);
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

	@Override
	public void stop() throws Exception {

	}

	@Override
	public List<Service> getServices() {
		return null;
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
