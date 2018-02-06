package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserStatus;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatRoomObserver;

/**
 * Created by danilovey on 05.02.2018.
 */
public interface ChatRoomPreviewVc extends BaseView<ChatRoomPreviewVc, Parent>, ChatRoomObserver {
    void setSelected(boolean selected);
    boolean isSelected();

    void setTitle(String titleLabel);
    void setStatus(ChatUserStatus statusLabel);
}
