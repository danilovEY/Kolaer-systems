package ru.kolaer.client.javafx;

import java.util.concurrent.TimeUnit;

import org.controlsfx.dialog.ProgressDialog;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.system.ProgressBarObservable;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitImpl;

public class TestUI extends Application {
	private UniformSystemEditorKit uniformSystemEditorKit;

	@Ignore
	@Test
	public void testDialog() throws InterruptedException {
		Platform.runLater(() -> {
			final ProgressDialog prog = new ProgressDialog(new Service<Void>() {

				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {
						@Override
						protected Void call() throws InterruptedException {
							updateMessage("Initializing Task");
							updateProgress(0, 10);
							for(int i = 0; i < 10; i++){
								Thread.sleep(300);
								// DO BACKGROUND WORK HERE

								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										// DO LIVE SCENE GRAPH WORK HERE
									}
								});

								updateProgress(i + 1, 10);
								updateMessage("Progress Msg");
							}
							updateMessage("Finished");
							return null;
						}
					};
				}

			});
			prog.showAndWait();

		});
		TimeUnit.SECONDS.sleep(10);
	}

	@Test
	public void testDialogLoading() throws InterruptedException {

		final Service<Void> service = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						this.updateMessage("AAAA");
						
						for(int i = 0; i < 10; i++){
							this.updateProgress(i, 10);
							System.out.println(this.isCancelled());
							TimeUnit.SECONDS.sleep(1);

						}
						return null;
					}

				};
			}

		};
		service.start();

		this.uniformSystemEditorKit.getUISystemUS().getDialog().showLoadingDialog(service);

		TimeUnit.SECONDS.sleep(10);
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
