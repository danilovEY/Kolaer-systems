package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatObserver;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.02.2018.
 */
public interface ChatRoomListVc extends BaseView<ChatRoomListVc, Parent>, ChatObserver {
    void handlerInfo(ChatInfoUserActionDto infoUserActionDto);
    void handlerInfo(ChatInfoRoomActionDto chatInfoRoomActionDto);

    void addChatRoomDto(List<ChatRoomDto> chatRoomDtoList);
    void addChatRoomDto(ChatRoomDto chatRoomDto);

    void setOnSelectRoom(Consumer<ChatRoomVc> consumer);
}
