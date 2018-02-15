package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageTable {
    ServerResponse<NotifyMessageDto> getLastNotifyMessage();
    ServerResponse addNotifyMessage(NotifyMessageDto notifyMessage);

    ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages();

    ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages(int page, int pageSize);
}
