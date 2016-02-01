package ru.kolaer.client.javafx;


import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.system.ProgressBarObservable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitImpl;

public class TestUI extends Application {
	private UniformSystemEditorKit uniformSystemEditorKit;
	
	@Test
	public void testDialogLoading() throws InterruptedException {
		final ProgressBarObservable prog = this.uniformSystemEditorKit.getUISystemUS().getDialog().showLoadingDialog("456");
		TimeUnit.SECONDS.sleep(1);
		prog.setValue(-2);
		TimeUnit.SECONDS.sleep(2);
	}
	
	@BeforeClass
	public static void setUp() throws InterruptedException {
	    System.out.printf("About to launch FX App\n");
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(TestUI.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    System.out.printf("FX App thread started\n");
	}
	
	@Before
	public void initSys() {
		this.uniformSystemEditorKit = new UniformSystemEditorKitImpl();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		 
	}
}
