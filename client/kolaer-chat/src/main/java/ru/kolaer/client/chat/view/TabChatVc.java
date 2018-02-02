package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatInfoHandler;
import ru.kolaer.client.chat.service.ChatObserver;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface TabChatVc extends BaseView<TabChatVc, Parent>, ChatObserver, ChatInfoHandler {
    TabChatRoomVc showChatRoom(TabChatRoomVc tabChatRoomVc, boolean focus);
    TabChatRoomVc getChatRoom(ChatRoomDto chatRoomDto);
    boolean roomIsShow(TabChatRoomVc tabChatRoomVc);

    boolean roomIsFocus(TabChatRoomVc chatRoom);
}
