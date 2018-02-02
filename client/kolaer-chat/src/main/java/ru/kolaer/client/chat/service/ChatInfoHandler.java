package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.lang.reflect.Type;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatInfoHandler extends StompSessionHandler, Subscription {
    default Type getPayloadType(StompHeaders headers) {
        return ChatInfoDto.class;
    }

    default void afterConnected(StompSession session, StompHeaders connectedHeaders){}

    default void handleFrame(StompHeaders headers, Object payload) {
        handleFrame(headers, (ChatInfoDto) payload);
    }

    default void handleFrame(StompHeaders headers, ChatInfoDto info) {
        if(info != null){
            if (info.getCommand() == ChatInfoCommand.CREATE_NEW_ROOM) {
                ChatInfoCreateNewRoomDto chatInfoCreateNewRoomDto = new ChatInfoCreateNewRoomDto();
                chatInfoCreateNewRoomDto.setData((ChatRoomDto) info.getData());
                chatInfoCreateNewRoomDto.setCommand(info.getCommand());
                chatInfoCreateNewRoomDto.setId(info.getId());
                chatInfoCreateNewRoomDto.setAccountId(info.getAccountId());
                chatInfoCreateNewRoomDto.setCreateInfo(info.getCreateInfo());

                handlerNewRoom(chatInfoCreateNewRoomDto);
            } else if(info.getCommand() == ChatInfoCommand.CONNECT || info.getCommand() == ChatInfoCommand.DISCONNECT){
                ChatInfoUserActionDto chatInfoUserActionDto = new ChatInfoUserActionDto();
                chatInfoUserActionDto.setData((ChatUserDto) info.getData());
                chatInfoUserActionDto.setCommand(info.getCommand());
                chatInfoUserActionDto.setId(info.getId());
                chatInfoUserActionDto.setAccountId(info.getAccountId());
                chatInfoUserActionDto.setCreateInfo(info.getCreateInfo());

                handlerUserAction(chatInfoUserActionDto);
            } else {
                handlerInfo(info);
            }
        }
    }

    void handlerInfo(ChatInfoDto chatInfoDto);
    void handlerNewRoom(ChatInfoCreateNewRoomDto chatInfoCreateNewRoomDto);
    void handlerUserAction(ChatInfoUserActionDto chatInfoUserActionDto);
}
