package ru.kolaer.common.system.network.kolaerweb;

import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface ServerTools {
    ServerResponse<LocalDateTime> getCurrentDataTime();
}
