package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface EmployeeService extends ServiceBase<EmployeeEntity> {
    List<EmployeeEntity> getUserRangeBirthday(Date startData, Date endData);
    List<EmployeeEntity> getUsersByBirthday(Date date);
    List<EmployeeEntity> getUserBirthdayToday();
    List<EmployeeEntity> getUsersByInitials(String initials);
    List<EmployeeEntity> getUsersByDepartamentId(Integer id);
    Page<EmployeeEntity> getUsersByDepartamentId(int page, int pageSize, Integer id);
    int getCountUserBirthday(Date date);
}
