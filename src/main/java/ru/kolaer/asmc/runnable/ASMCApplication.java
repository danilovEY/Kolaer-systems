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
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.system.ProgressBarObservable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

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
	public void run() throws Exception {
		if(this.pane == null){
			final ProgressBarObservable progress = this.editorKid.getUISystemUS().getDialog().showLoadingDialog("Загрузка компонентов");
			SettingSingleton.initialization();
			try(final InputStream stream = Resources.V_MAIN_FRAME.openStream()){
				final FXMLLoader loader = new FXMLLoader();
				this.pane = loader.load(stream);
				final InputStream inputStream = this.getClass().getResourceAsStream("/resources/CSS/Default/Default.css");
				final URL inputStreamUrl = this.getClass().getResource("/resources/CSS/Default/Default.css");
				this.pane.getStylesheets().addAll(inputStreamUrl.toExternalForm());
				inputStream.close();
				Platform.runLater(() -> {
					root.setCenter(pane);
					root.setPrefSize(800, 600);	
					progress.setValue(-2);
				});
			}catch(final MalformedURLException e){
				throw e;
			}catch(final IOException e){
				throw e;
			}
		}
	}
}
