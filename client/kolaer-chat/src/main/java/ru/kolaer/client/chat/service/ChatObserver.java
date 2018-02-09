package ru.kolaer.client.chat.service;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface ChatObserver {
    void connect(ChatClient chatClient);
    void disconnect(ChatClient chatClient);
    void close(ChatClient chatClient);
}
