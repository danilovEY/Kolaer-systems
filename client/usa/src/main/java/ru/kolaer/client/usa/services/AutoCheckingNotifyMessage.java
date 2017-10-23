package ru.kolaer.client.usa.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 19.08.2016.
 */
public class AutoCheckingNotifyMessage implements Service {
    private static final Logger log = LoggerFactory.getLogger(AutoCheckingNotifyMessage.class);

    private boolean run = false;
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
                    UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                            .showInformationNotifyAdmin("Сообщение!", lastNotifyMessage.getMessage(), Collections.emptyList());
                }
            } else {
                ServerExceptionMessage exceptionMessage = responseLastNotifyMessage.getExceptionMessage();
                log.error(exceptionMessage.toString());
                UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                        .showErrorNotify("Невозможно получить сообщение с сервера!", exceptionMessage.getMessage());
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
