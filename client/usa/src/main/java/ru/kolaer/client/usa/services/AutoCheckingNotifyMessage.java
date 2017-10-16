package ru.kolaer.client.usa.services;

import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 19.08.2016.
 */
public class AutoCheckingNotifyMessage implements Service {
    private boolean run = false;
    private boolean error = false;
    private NotifyMessageDto lastNotifyMessage;
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
        run = false;
    }

    @Override
    public void run() {
        run = true;

        while (run) {
                ServerResponse<NotifyMessageDto> responseLastNotifyMessage = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getNotifyMessageTable()
                        .getLastNotifyMessage();
                if(!responseLastNotifyMessage.isServerError()) {
                    NotifyMessageDto response = responseLastNotifyMessage.getResponse();
                    if(lastNotifyMessage == null || !lastNotifyMessage.getMessage().equals(response.getMessage())) {
                        lastNotifyMessage = response;
                        UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showInformationNotifiAdmin("Сообщение!", lastNotifyMessage.getMessage());
                    }
                } else {
                    if(!error) {
                        UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showErrorNotifi("Невозможно получить сообщение с сервера!", ""); //TODO: !!!
                        error = true;
                    }
                }
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                run = false;
                return;
            }
        }
    }
}
