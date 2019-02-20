package ru.kolaer.asmc.mvp.service;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.asmc.mvp.model.DataService;
import ru.kolaer.asmc.mvp.model.DataServiceObserver;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.tools.Application;
import ru.kolaer.client.core.plugins.services.Service;

import java.util.List;
import java.util.stream.Stream;


@Slf4j
public class AutoRunService implements Service, DataServiceObserver {
    private final DataService dataService;
    private boolean run;

    public AutoRunService(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public boolean isRunning() {
        return run;
    }

    @Override
    public String getName() {
        return "Авто запуск";
    }

    @Override
    public void stop() {
        this.run = false;
    }

    @Override
    public void run() {
        this.run = true;

        this.updateData(dataService.getModel());
    }

    private void execute(MLabel mLabel) {
        new Application(mLabel.getPathApplication(), mLabel.getPathOpenAppWith()).start();
    }

    private Stream<MLabel> getLabels(MGroup currentGroup) {
        if (currentGroup.getGroups() == null || currentGroup.getGroups().isEmpty()) {
            return currentGroup.getLabelList().stream();
        } else {
            return currentGroup.getGroups()
                    .stream()
                    .flatMap(this::getLabels);
        }
    }

    @Override
    public void updateData(List<MGroup> groupList) {
        if (run && groupList != null && !groupList.isEmpty()) {
            groupList.stream()
                    .flatMap(this::getLabels)
                    .filter(MLabel::isAutoRun)
                    .forEach(this::execute);

            stop();
        }
    }

    @Override
    public void saveData() {

    }
}
