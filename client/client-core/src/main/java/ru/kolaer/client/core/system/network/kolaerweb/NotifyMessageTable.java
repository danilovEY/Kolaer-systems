package ru.kolaer.client.core.system.network.kolaerweb;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageTable {
    ServerResponse<NotifyMessageDto> getLastNotifyMessage();
    ServerResponse addNotifyMessage(NotifyMessageDto notifyMessage);

    ServerResponse<PageDto<NotifyMessageDto>> getAllNotifyMessages();

    ServerResponse<PageDto<NotifyMessageDto>> getAllNotifyMessages(int page, int pageSize);
}
