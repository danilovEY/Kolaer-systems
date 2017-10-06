package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface EmployeeService extends ServiceBase<EmployeeDto> {
    EmployeeDto getByPersonnelNumber(Integer id);

    List<EmployeeDto> getUserRangeBirthday(Date startData, Date endData);
    List<EmployeeDto> getUsersByBirthday(Date date);
    List<EmployeeDto> getUserBirthdayToday();
    List<EmployeeDto> getUsersByInitials(String initials);
    List<EmployeeDto> getUsersByDepartmentId(Integer id);
    Page<EmployeeDto> getUsersByDepartmentId(int page, int pageSize, Integer id);
    int getCountUserBirthday(Date date);
}
