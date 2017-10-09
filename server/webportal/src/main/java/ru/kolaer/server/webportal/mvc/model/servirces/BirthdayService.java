package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface BirthdayService<T extends BaseDto> {
    List<T> getUserRangeBirthday(Date startData, Date endData);
    List<T> getUsersByBirthday(Date date);
    List<T> getUserBirthdayToday();
    List<T> getUsersByInitials(String initials);
    int getCountUserBirthday(Date date);

}
