package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface EmployeeService extends ServiceBase<GeneralEmployeesEntity> {
    List<GeneralEmployeesEntity> getUserRangeBirthday(Date startData, Date endData);
    List<GeneralEmployeesEntity> getUsersByBirthday(Date date);
    List<GeneralEmployeesEntity> getUserBirthdayToday();
    List<GeneralEmployeesEntity> getUsersByInitials(String initials);
    List<GeneralEmployeesEntity> getUsersByDepartamentId(Integer id);
    Page<GeneralEmployeesEntity> getUsersByDepartamentId(int page, int pageSize, Integer id);
    int getCountUserBirthday(Date date);
}
