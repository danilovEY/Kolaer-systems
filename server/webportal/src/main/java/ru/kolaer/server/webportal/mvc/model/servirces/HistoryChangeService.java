package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.dto.HistoryChangeDto;
import ru.kolaer.server.webportal.mvc.model.entities.historychange.HistoryChangeEvent;

public interface HistoryChangeService {
    HistoryChangeDto createHistoryChange(String valueOld, String valueNew, HistoryChangeEvent event);
    HistoryChangeDto createHistoryChange(HistoryChangeEvent event);

}
