package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Доа для работы с сотрудниками.
 */
public interface EmployeeDao extends DaoStandard<GeneralEmployeesEntity> {
    List<GeneralEmployeesEntity> findEmployeeByInitials(String initials);
}
