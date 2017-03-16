package ru.kolaer.client.wer.mvp.model;

import java.util.List;

/**
 * Created by danilovey on 16.03.2017.
 */
public interface ObservableEventLoader {
    void registerObserver(ObserverEventLoader observerEventLoader);
    void removeObserver(ObserverEventLoader observerEventLoader);
    void notifyObserver(List<Event> eventList);
}
