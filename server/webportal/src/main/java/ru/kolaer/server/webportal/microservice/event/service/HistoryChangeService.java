package ru.kolaer.server.webportal.microservice.event.service;

import ru.kolaer.server.webportal.microservice.event.HistoryChangeEvent;
import ru.kolaer.server.webportal.microservice.event.dto.HistoryChangeDto;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

public interface HistoryChangeService {
    <T extends BaseEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(HistoryChangeEvent event);
    String objectToJson(Object obj);

}
