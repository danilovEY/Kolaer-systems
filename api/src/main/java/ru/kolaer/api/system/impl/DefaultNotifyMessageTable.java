package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.kolaerweb.NotifyMessageTable;

import java.util.Optional;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultNotifyMessageTable implements NotifyMessageTable {

    @Override
    public ServerResponse<NotifyMessageDto> getLastNotifyMessage() {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse addNotifyMessage(NotifyMessageDto notifyMessage) {
        log.info("Добавлено сообщение: {}", Optional.ofNullable(notifyMessage)
                .orElse(new NotifyMessageDto()).getMessage());
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages() {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages(int page, int pageSize) {
        return ServerResponse.createServerResponse();
    }
}
