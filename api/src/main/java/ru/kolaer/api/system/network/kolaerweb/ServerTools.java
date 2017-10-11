package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface ServerTools {
    ServerResponse<LocalDateTime> getCurrentDataTime();
}
