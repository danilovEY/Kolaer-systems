package ru.kolaer.server.webportal.microservice.event;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;

import java.time.LocalDateTime;

@Data
public class HistoryChangeDto {
    private String valueOld;
    private String valueNew;
    private LocalDateTime eventDate;
    private AccountSimpleDto account;
    private HistoryChangeEvent event = HistoryChangeEvent.UNKNOWN;
}
