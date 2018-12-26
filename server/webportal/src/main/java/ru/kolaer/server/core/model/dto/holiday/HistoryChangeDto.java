package ru.kolaer.server.core.model.dto.holiday;

import lombok.Data;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.server.core.model.entity.historychange.HistoryChangeEvent;

import java.time.LocalDateTime;

@Data
public class HistoryChangeDto {
    private String valueOld;
    private String valueNew;
    private LocalDateTime eventDate;
    private AccountSimpleDto account;
    private HistoryChangeEvent event = HistoryChangeEvent.UNKNOWN;
}
