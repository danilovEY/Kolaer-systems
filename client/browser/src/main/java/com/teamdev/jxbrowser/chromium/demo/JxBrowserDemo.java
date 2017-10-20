package com.teamdev.jxbrowser.chromium.demo;

import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public class JxBrowserDemo implements UniformSystemPlugin {
	private boolean isRun = false;
	private BorderPane mainPane;
	private TabPanel tabPane;
	private UniformSystemEditorKit editorKit;

	private static void initEnvironment() {
		System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JxBrowser Demo");
	}
	
	private void initUI() {		
		this.tabPane = new TabPanel();
		this.mainPane = new BorderPane(this.tabPane);
		final Button createTabBut = new Button("Создать вкладку");
		createTabBut.setOnAction(e -> {
			this.loadURL("google.ru");
		});

		this.mainPane.setTop(createTabBut);
	}

	@Override
	public void start() throws Exception {

	}

	public void loadURL(final String url) {
		while(this.tabPane == null) {
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		Tools.runOnThreadFXAndWain(() -> {
			this.tabPane.addTab(new TabWithBrowser(this.tabPane, url));
		},20, TimeUnit.SECONDS);
	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void initialization(UniformSystemEditorKit editorKit) throws Exception {
		this.editorKit = editorKit;

		initEnvironment();
		final String dirBrowser = "jxbrowser-chromium-43";
		final File dir = new File(dirBrowser);

		if(!dir.exists()) {
			dir.mkdirs();

			this.getClass().getResource("/chromium-windows.zip");
		}

		BrowserPreferences.setChromiumDir(System.getProperty("user.dir") + "/" + dirBrowser);

		/*LoggerProvider.getBrowserLogger().setLevel(Level.WARNING);
		LoggerProvider.getIPCLogger().setLevel(Level.WARNING);
		LoggerProvider.getChromiumProcessLogger().setLevel(Level.WARNING);
		LoggerProvider.setLevel(Level.OFF);*/
	}

	@Override
	public List<Service> getServices() {
		return null;
	}

	@Override
	public void updatePluginObjects(String key, Object object) {
		if(key.equals("url")) {
			this.loadURL(object.toString());
		}
	}

	@Override
	public void setContent(Parent content) {
		
	}

	@Override
	public Parent getContent() {
		return this.mainPane;
	}

	@Override
	public URL getIcon() {
		return null;
	}

	@Override
	public void initView(Function<Parent, Void> viewVisit) throws Exception {
		if(!isRun) {
			isRun = true;

			this.editorKit.getUISystemUS().getNotification().showInformationNotifi("Внимание!", "Инсталяция браузера, подождите...", Duration.seconds(3), Pos.CENTER);

			Tools.runOnThreadFXAndWain(() -> {
				this.initUI();
			},20, TimeUnit.SECONDS);
		}
	}
}
