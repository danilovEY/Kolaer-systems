package ru.kolaer.client.wer.mvp.model;

import java.util.List;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface ObserverEventLoader {
    void updateEvent(List<Event> eventList);
}
