package ru.kolaer.asmc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.controller.CMainFrame;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Danilov on 14.05.2016.
 */
public class AutoCheckDataService implements Service {
    private final Logger LOG = LoggerFactory.getLogger(AutoCheckDataService.class);

    private final String pathDitSerializedObject = "data";
    private final String fileNameSerializeObjects = "objects.aer";
    private final File fileSer = new File(pathDitSerializedObject + "/" + fileNameSerializeObjects);
    private CMainFrame cMainFrame;
    private long lastMod = 0;
    private boolean isRun = false;
    private final UniformSystemEditorKit editorKit;

    public AutoCheckDataService(final UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
    }

    public void setcMainFrame(final CMainFrame cMainFrame) {
        this.cMainFrame = cMainFrame;
    }

    @Override
    public boolean isRunning() {
        return this.isRun;
    }

    @Override
    public String getName() {
        return "Автообновление data.obj";
    }

    @Override
    public void stop() {
        this.isRun = false;
    }

    @Override
    public void run() {
        this.isRun = true;

        while(this.isRun) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                LOG.error("Время ошидание прервано!");
                this.isRun = false;
                return;
            }

            if(!this.fileSer.exists()) {
                continue;
            }

            if(this.lastMod == 0) {
                this.lastMod = this.fileSer.lastModified();
                continue;
            }

            if(this.fileSer.lastModified() != this.lastMod) {
                LOG.info("isSave: {}", SettingSingleton.getInstance().isSave());
                if(this.cMainFrame != null && !SettingSingleton.getInstance().isSave()) {
                    this.editorKit.getUISystemUS().getNotification().showInformationNotifi("Обновление!","Обновление данных в АСУП...");
                    this.cMainFrame.updateData();
                }
                this.lastMod = this.fileSer.lastModified();
                SettingSingleton.getInstance().setSave(false);
            }
        }

    }
}
