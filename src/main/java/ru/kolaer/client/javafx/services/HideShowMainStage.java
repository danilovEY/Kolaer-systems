package ru.kolaer.client.javafx.services;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.runnable.Launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by Danilov on 12.05.2016.
 */
public class HideShowMainStage implements Service {
    private final Logger LOG = LoggerFactory.getLogger(HideShowMainStage.class);

    final File pathToFile = new File(Launcher.pathToRunFile);
    private final Stage mainStage;
    private long lastModif = 0;
    private boolean isRun = false;

    public HideShowMainStage(final Stage mainStage) {
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

    }

    @Override
    public void run() {
        if(!this.pathToFile.exists()) {
            try {
                LOG.info("Создание файла: {}", this.pathToFile.getAbsoluteFile());
                this.pathToFile.createNewFile();
            } catch (IOException e) {
                LOG.error("Невозможно создать файл: {}", this.pathToFile.getAbsoluteFile());
                return;
            }

            this.lastModif = this.pathToFile.lastModified();
        }

        this.isRun = true;

        while (this.isRun) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                LOG.error("Прерывание операции!");
                this.isRun = false;
                return;
            }

            if(this.pathToFile.lastModified() != lastModif) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(pathToFile)) {
                    fileOutputStream.write(0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lastModif = this.pathToFile.lastModified();

                Tools.runOnThreadFX(() -> {
                    //if(this.mainStage.isShowing()) {
                     //   this.mainStage.toFront();
                    //} else {
                    this.mainStage.setMaximized(true);
                    this.mainStage.requestFocus();
                    this.mainStage.show();
                    //}
                });



            }
        }

    }
}
