package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.system.network.kolaerweb.ServerTools;

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
