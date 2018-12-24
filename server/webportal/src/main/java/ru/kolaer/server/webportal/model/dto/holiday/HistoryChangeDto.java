package ru.kolaer.server.webportal.model.dto.holiday;

import lombok.Data;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.server.webportal.model.entity.historychange.HistoryChangeEvent;

import java.time.LocalDateTime;

@Data
public class HistoryChangeDto {
    private String valueOld;
    private String valueNew;
    private LocalDateTime eventDate;
    private AccountSimpleDto account;
    private HistoryChangeEvent event = HistoryChangeEvent.UNKNOWN;
}
