package ru.kolaer.asmc.mvp.service;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.plugins.services.Service;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.asmc.mvp.model.DataService;
import ru.kolaer.asmc.mvp.model.DataServiceObserver;
import ru.kolaer.asmc.mvp.model.MGroup;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 30.10.2017.
 */
@Slf4j
public class AutoUploadData implements Service, DataServiceObserver {
    private boolean run = false;
    private final DataService dataService;
    private boolean save = false;

    public AutoUploadData(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public boolean isRunning() {
        return run;
    }

    @Override
    public String getName() {
        return "Авто обновление данных";
    }

    @Override
    public void stop() {
        run = false;
    }

    @Override
    public void run() {
        run = true;

        long lastSave = dataService.getLastSave();

        while (run) {
            try {
                TimeUnit.SECONDS.sleep(5);

                if(save) {
                    save = false;
                    lastSave = dataService.getLastSave();
                } else if(lastSave != dataService.getLastSave()){
                    try{
                        lastSave = dataService.getLastSave();

                        UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getNotification()
                                .showInformationNotify("Обновление данных АСУП...", null);

                        dataService.loadData();

                        UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getNotification()
                                .showInformationNotify("Обновление выполнено!", null);
                    } catch (Exception ex) {
                        log.error("Ошибка при обновлении данных", ex);

                        UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getNotification()
                                .showErrorNotify("Не удалось обновить данные АСУП", null);
                    }
                }
            } catch (InterruptedException e) {
                log.error(null, e);
                run = false;
            }
        }
    }

    @Override
    public void updateData(List<MGroup> groupList) {}

    @Override
    public void saveData() {
        save = true;
    }
}
