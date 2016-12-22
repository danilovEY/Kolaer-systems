package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Доа для работы с сотрудниками.
 */
public interface EmployeeDao extends DefaultDao<GeneralEmployeesEntity>, BirthdayDao<GeneralEmployeesEntity> {
    List<GeneralEmployeesEntity> findEmployeeByInitials(String initials);

    List<GeneralEmployeesEntity> findByDepartamentById(Integer id);

    Page<GeneralEmployeesEntity> findByDepartamentById(int page, int pageSize, Integer id);
}
