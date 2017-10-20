package ru.kolaer.client.usa.services;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.usa.runnable.Launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Danilov on 12.05.2016.
 */
public class HideShowMainStage implements Service {
	private final Logger LOG = LoggerFactory.getLogger(HideShowMainStage.class);

	private final File pathToFile = new File(Launcher.pathToRunFile);
	private final File pathToShowAppFile = new File(Launcher.pathToShowAppFile);
	private final Stage mainStage;
	private boolean isRun = false;
	private FileInputStream lockFile;

	public HideShowMainStage(Stage mainStage) {
		if(mainStage == null)
			throw new IllegalArgumentException("Stage == null!");
		
		this.mainStage = mainStage;
	}

	@Override
	public boolean isRunning() {
		return isRun;
	}

	@Override
	public String getName() {
		return "Отображение главного окна";
	}

	@Override
	public void stop() {
		if(lockFile != null) {
			try {
				lockFile.close();
			} catch (IOException ex) {
				LOG.error("Не удалось закрыть поток файла: {}", pathToFile.getAbsoluteFile());
			}
		}
		pathToFile.delete();
		isRun = false;
	}

	@Override
	public void run() {
		if(!pathToFile.exists()){
			try {
				LOG.info("Создание файла: {}", pathToFile.getAbsoluteFile());

				if(pathToShowAppFile.exists()) {
					pathToShowAppFile.delete();
				}

				if(pathToFile.createNewFile()) {
					lockFile = new FileInputStream(pathToFile);
				}
			} catch(IOException e){
				LOG.error("Невозможно создать файл: {}", pathToFile.getAbsoluteFile());
				return;
			}
		}

		isRun = true;

		while(isRun){
			try{
				TimeUnit.MILLISECONDS.sleep(200);
			}catch(InterruptedException e){
				LOG.error("Прерывание операции!");
				stop();
				return;
			}

			if(pathToShowAppFile.exists()){
				pathToShowAppFile.delete();
				mainStage.show();
			}
		}
	}
}
