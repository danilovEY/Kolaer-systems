package ru.kolaer.server.core.service;

import ru.kolaer.server.core.model.dto.holiday.HistoryChangeDto;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.core.model.entity.historychange.HistoryChangeEvent;

public interface HistoryChangeService {
    <T extends DefaultEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(HistoryChangeEvent event);
    String objectToJson(Object obj);

}
