package ru.kolaer.server.core.dao;

import ru.kolaer.server.core.model.entity.DefaultEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by danilovey on 13.09.2016.
 */
public interface BirthdayDao<T extends DefaultEntity> {
    List<T> getUserRangeBirthday(LocalDate startData, LocalDate endData);
    List<T> getUsersByBirthday(LocalDate date);
    List<T> getUserBirthdayToday();
    List<T> getUsersByInitials(String initials);
    int getCountUserBirthday(LocalDate date);
}
