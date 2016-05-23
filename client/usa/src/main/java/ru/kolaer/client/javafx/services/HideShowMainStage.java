package ru.kolaer.client.javafx.services;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.runnable.Launcher;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Danilov on 12.05.2016.
 */
public class HideShowMainStage implements Service {
	private final Logger LOG = LoggerFactory.getLogger(HideShowMainStage.class);

	private final File pathToFile = new File(Launcher.pathToRunFile);
	private final Stage mainStage;
	private boolean isRun = false;

	public HideShowMainStage(final Stage mainStage) {
		if(mainStage == null)
			throw new IllegalArgumentException("Stage == null!");
		
		this.mainStage = mainStage;
	}

	@Override
	public boolean isRunning() {
		return this.isRun;
	}

	@Override
	public String getName() {
		return "Отображение главного окна";
	}

	@Override
	public void stop() {
		pathToFile.delete();
		this.isRun = false;
	}

	@Override
	public void run() {
		if(!this.pathToFile.exists()){
			try{
				LOG.info("Создание файла: {}", this.pathToFile.getAbsoluteFile());
				this.pathToFile.createNewFile();
			}catch(IOException e){
				LOG.error("Невозможно создать файл: {}", this.pathToFile.getAbsoluteFile());
				return;
			}
		}

		this.isRun = true;

		while(this.isRun){
			try{
				TimeUnit.MILLISECONDS.sleep(200);
			}catch(InterruptedException e){
				LOG.error("Прерывание операции!");
				this.isRun = false;
				return;
			}

			if(!this.pathToFile.exists()){
				try{
					this.pathToFile.createNewFile();

					Tools.runOnThreadFX(() -> {
						this.mainStage.setMaximized(true);
						this.mainStage.requestFocus();
						this.mainStage.show();
					});
				}catch(IOException e){
					LOG.error("Невозможно создать файл!");
					this.isRun = false;
					return;
				}
			}
		}
	}
}
