package ru.kolaer.client.counter.service;

import ru.kolaer.common.mvp.model.kolaerweb.CounterDto;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface CounterView {
    void setTitle(String title);
    void setDescription(String des);
    void setFoot(String foot);
    void setTime(int month, int day, int hours, int min, int sec);
    CounterDto getCounter();
}
