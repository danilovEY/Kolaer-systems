package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.dto.ResultUpdateEmployeesDto;

import java.io.File;
import java.io.InputStream;

/**
 * Created by danilovey on 25.01.2017.
 */
public interface UpdateEmployeesDao {
    ResultUpdateEmployeesDto updateEmployeesFromXlsx(File file);

    ResultUpdateEmployeesDto updateEmployeesFromXlsx(InputStream inputStream);
}
