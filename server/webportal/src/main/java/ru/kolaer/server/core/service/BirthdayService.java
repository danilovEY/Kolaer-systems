package ru.kolaer.server.core.service;


import ru.kolaer.common.dto.BaseDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface BirthdayService<T extends BaseDto> {
    List<T> getUserRangeBirthday(LocalDate startData, LocalDate endData);
    List<T> getUsersByBirthday(LocalDate date);
    List<T> getUserBirthdayToday();
    List<T> getUsersByInitials(String initials);
    int getCountUserBirthday(LocalDate date);

}
