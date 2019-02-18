package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.ChatRoomObserver;
import ru.kolaer.client.core.mvp.view.BaseView;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserStatus;

/**
 * Created by danilovey on 05.02.2018.
 */
public interface ChatRoomPreviewVc extends BaseView<ChatRoomPreviewVc, Parent>, ChatRoomObserver, ChatObserver {
    void setSelected(boolean selected);
    boolean isSelected();

    void setTitle(String titleLabel);
    void setStatus(ChatUserStatus statusLabel);
}
