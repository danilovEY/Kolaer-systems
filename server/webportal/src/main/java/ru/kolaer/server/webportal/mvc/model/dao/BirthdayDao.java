package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface BirthdayDao<T extends BaseEntity> {
    List<T> getUserRangeBirthday(Date startData, Date endData);
    List<T> getUsersByBirthday(Date date);
    List<T> getUserBirthdayToday();
    List<T> getUsersByInitials(String initials);
    int getCountUserBirthday(Date date);
}
