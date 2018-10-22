package ru.kolaer.server.webportal.microservice.event;

import ru.kolaer.server.webportal.microservice.event.HistoryChangeDto;
import ru.kolaer.server.webportal.common.entities.BaseEntity;
import ru.kolaer.server.webportal.microservice.event.HistoryChangeEvent;

public interface HistoryChangeService {
    <T extends BaseEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(HistoryChangeEvent event);
    String objectToJson(Object obj);

}
