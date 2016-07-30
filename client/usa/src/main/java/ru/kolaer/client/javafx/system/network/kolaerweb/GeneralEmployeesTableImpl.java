package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;

import java.util.Date;

/**
 * Created by Danilov on 31.07.2016.
 */
public class GeneralEmployeesTableImpl implements GeneralEmployeesTable {
    @Override
    public GeneralEmployeesEntity[] getAllUser() {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersMax(int maxCount) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByBirthday(Date date) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByRengeBirthday(Date dateBegin, Date dateEnd) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersBirthdayToday() {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public GeneralEmployeesEntity[] getUsersByInitials(String initials) {
        return new GeneralEmployeesEntity[0];
    }

    @Override
    public int getCountUsersBirthday(Date date) {
        return 0;
    }
}
