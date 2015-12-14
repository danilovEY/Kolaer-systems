package ru.kolaer.asmc.runnable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.client.javafx.plugins.IApplication;

public class ASMCApplication implements IApplication {
	private AnchorPane root;
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
	public void run() {
		SettingSingleton.initialization();
		try {
			root = FXMLLoader.load(URI.create(Resources.V_MAIN_FRAME).toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		
	}

}
