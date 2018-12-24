package ru.kolaer.server.webportal.service;

import ru.kolaer.server.webportal.model.dto.holiday.HistoryChangeDto;
import ru.kolaer.server.webportal.model.entity.BaseEntity;
import ru.kolaer.server.webportal.model.entity.historychange.HistoryChangeEvent;

public interface HistoryChangeService {
    <T extends BaseEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(HistoryChangeEvent event);
    String objectToJson(Object obj);

}
