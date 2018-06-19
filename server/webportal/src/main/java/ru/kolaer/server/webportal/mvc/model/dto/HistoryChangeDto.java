package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEvent;

import java.time.LocalDateTime;

@Data
public class HistoryChangeDto {
    private String valueOld;
    private String valueNew;
    private LocalDateTime eventDate;
    private AccountSimpleDto account;
    private HistoryChangeEvent event = HistoryChangeEvent.UNKNOWN;
}
