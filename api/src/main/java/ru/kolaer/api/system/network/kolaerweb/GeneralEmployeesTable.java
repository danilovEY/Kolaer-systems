package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.exeptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.Date;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface GeneralEmployeesTable {
    GeneralEmployeesEntity[] getAllUser() throws ServerException;
    GeneralEmployeesEntity[] getUsersMax(int maxCount) throws ServerException;
    GeneralEmployeesEntity[] getUsersByBirthday(Date date) throws ServerException;
    GeneralEmployeesEntity[] getUsersByRangeBirthday(Date dateBegin, Date dateEnd) throws ServerException;
    GeneralEmployeesEntity[] getUsersBirthdayToday() throws ServerException;
    GeneralEmployeesEntity[] getUsersByInitials(String initials) throws ServerException;
    int getCountUsersBirthday(Date date) throws ServerException;
}
