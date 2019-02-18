package ru.kolaer.client.core.system.impl;

import ru.kolaer.client.core.system.network.kolaerweb.ServerTools;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultServerTools implements ServerTools {
    @Override
    public ServerResponse<LocalDateTime> getCurrentDataTime() {
        return ServerResponse.createServerResponse(LocalDateTime.now());
    }
}
