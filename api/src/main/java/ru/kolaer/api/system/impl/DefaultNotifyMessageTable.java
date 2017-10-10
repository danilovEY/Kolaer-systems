package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.system.network.kolaerweb.NotifyMessageTable;

import java.util.Optional;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultNotifyMessageTable implements NotifyMessageTable {

    @Override
    public NotifyMessageDto getLastNotifyMessage() throws ServerException {
        return null;
    }

    @Override
    public void addNotifyMessage(NotifyMessageDto notifyMessage) throws ServerException {
        log.info("Добавлено сообщение: {}", Optional.ofNullable(notifyMessage)
                .orElse(new NotifyMessageDto()).getMessage());
    }
}
