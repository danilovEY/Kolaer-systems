package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.system.network.UserDataBase;

import java.util.Date;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface GeneralEmployeesTable extends UserDataBase<EmployeeDto> {
    int getCountUsersBirthday(Date date) throws ServerException;
}
