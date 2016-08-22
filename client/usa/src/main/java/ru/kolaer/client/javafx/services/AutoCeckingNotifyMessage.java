package ru.kolaer.client.javafx.services;

import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitSingleton;

import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 19.08.2016.
 */
public class AutoCeckingNotifyMessage implements Service {
    private boolean run = false;
    private NotifyMessage lastNotifyMessage;
    @Override
    public boolean isRunning() {
        return this.run;
    }

    @Override
    public String getName() {
        return "Получение системного сообщения.";
    }

    @Override
    public void stop() {
        this.run = false;
    }

    @Override
    public void run() {
        this.run = true;

        while (this.run) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showInformationNotifiAdmin("Ошибка!", e.getMessage());
                this.run = false;
                return;
            }

            final NotifyMessage lastNotifyMessage = UniformSystemEditorKitSingleton.getInstance().getUSNetwork().getKolaerWebServer().getApplicationDataBase().getNotifyMessageTable().getLastNotifyMessage();
            if(this.lastNotifyMessage == null || !this.lastNotifyMessage.getMessage().equals(lastNotifyMessage.getMessage())) {
                this.lastNotifyMessage = lastNotifyMessage;
                UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showWarningNotifiAdmin("Сообщение!", lastNotifyMessage.getMessage());
            }
        }
    }
}