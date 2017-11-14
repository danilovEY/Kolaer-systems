package ru.kolaer.client.chat.service;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface ChatObservable {
    void registerObserver(ChatObserver observer);
    void removeObserver(ChatObserver observer);
}
