package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdateEmployeesDto;

import java.io.File;
import java.io.InputStream;

/**
 * Created by danilovey on 27.01.2017.
 */
public interface UpdateEmployeesService {
    ResultUpdateEmployeesDto updateEployees(File file);
    ResultUpdateEmployeesDto updateEployees(InputStream inputStream);
}
