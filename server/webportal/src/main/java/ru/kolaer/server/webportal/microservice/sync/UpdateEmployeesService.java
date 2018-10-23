package ru.kolaer.server.webportal.microservice.sync;

import ru.kolaer.server.webportal.microservice.event.dto.HistoryChangeDto;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by danilovey on 27.01.2017.
 */
public interface UpdateEmployeesService {
    List<HistoryChangeDto> updateEmployees(File file);
    List<HistoryChangeDto> updateEmployees(InputStream inputStream);
}
