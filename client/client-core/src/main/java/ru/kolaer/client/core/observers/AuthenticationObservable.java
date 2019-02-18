package ru.kolaer.client.core.observers;

/**
 * Created by danilovey on 03.08.2016.
 */
public interface AuthenticationObservable {
    void registerObserver(AuthenticationObserver observer);
    void removeObserver(AuthenticationObserver observer);
}
