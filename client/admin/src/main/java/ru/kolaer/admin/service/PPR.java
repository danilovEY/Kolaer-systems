package ru.kolaer.admin.service;

import ru.kolaer.api.mvp.model.kolaerweb.Counter;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface PPR {
    void setTitle(String title);
    void setDescription(String des);
    void setFoot(String foot);
    void setTime(int month, int day, int hours, int min, int sec);
    Counter getCounter();
}
