package ru.kolaer.client.chat.view;

import ru.kolaer.client.chat.service.ChatRoomObserver;

/**
 * Created by danilovey on 12.12.2017.
 */
public interface NotificationMessage extends ChatRoomObserver {
    void clear();
}
