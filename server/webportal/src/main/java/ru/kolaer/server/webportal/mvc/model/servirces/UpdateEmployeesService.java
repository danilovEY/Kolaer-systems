package ru.kolaer.server.webportal.mvc.model.servirces;

import java.io.File;
import java.io.InputStream;

/**
 * Created by danilovey on 27.01.2017.
 */
public interface UpdateEmployeesService {
    void updateEmployees(File file);
    void updateEmployees(InputStream inputStream);
}
