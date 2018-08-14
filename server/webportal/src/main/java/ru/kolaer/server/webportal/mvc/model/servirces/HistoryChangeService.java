package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.dto.holiday.HistoryChangeDto;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEvent;

public interface HistoryChangeService {
    <T extends BaseEntity> HistoryChangeDto createHistoryChange(T valueOld, T valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(HistoryChangeEvent event);
    String objectToJson(Object obj);

}
