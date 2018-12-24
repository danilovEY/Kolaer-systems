package ru.kolaer.common.system.impl;

import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.system.network.kolaerweb.ServerTools;

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
