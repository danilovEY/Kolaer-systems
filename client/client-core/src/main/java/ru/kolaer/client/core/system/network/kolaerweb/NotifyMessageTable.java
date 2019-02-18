package ru.kolaer.client.core.system.network.kolaerweb;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageTable {
    ServerResponse<NotifyMessageDto> getLastNotifyMessage();
    ServerResponse addNotifyMessage(NotifyMessageDto notifyMessage);

    ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages();

    ServerResponse<Page<NotifyMessageDto>> getAllNotifyMessages(int page, int pageSize);
}
