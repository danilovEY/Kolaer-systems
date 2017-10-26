package ru.kolaer.asmc.mvp.model;

/**
 * Created by danilovey on 26.10.2017.
 */
public interface DataServiceObservable {
    void registerObserver(DataServiceObserver observer);
    void removeObserver(DataServiceObserver observer);
}
