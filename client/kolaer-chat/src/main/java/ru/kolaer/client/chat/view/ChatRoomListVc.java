package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatObserver;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.02.2018.
 */
public interface ChatRoomListVc extends BaseView<ChatRoomListVc, Parent>, ChatObserver {
    void addChatRoomVc(List<ChatRoomVc> chatRoomVcList);
    void addChatRoomVc(ChatRoomVc chatRoomVc);

    void setOnSelectRoom(Consumer<ChatRoomVc> consumer);
    void setSelectRoom(ChatRoomVc chatRoomVc);
}
