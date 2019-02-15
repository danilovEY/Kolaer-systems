package ru.kolaer.client.chat.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.StringUtils;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatRoomObserver;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolchat.*;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 06.02.2018.
 */
@Slf4j
public class ChatRoomVcImpl implements ChatRoomVc {
    private final List<ChatRoomObserver> chatRoomObserverList = new ArrayList<>();
    private final ChatRoomDto chatRoomDto;

    private ChatClient chatClient;
    private String subscriptionId;

    private final ChatRoomPreviewVc chatRoomPreviewVc;
    private final ChatRoomMessagesVc chatRoomMessagesVc;

    public ChatRoomVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
        this.chatRoomMessagesVc = new ChatRoomMessagesVcImpl(chatRoomDto);
        this.chatRoomPreviewVc = new ChatRoomPreviewVcImpl(chatRoomDto);

        this.registerChatRoomObserver(chatRoomMessagesVc);
        this.registerChatRoomObserver(chatRoomPreviewVc);

        this.init();
    }

    @Override
    public void handlerInfo(ChatInfoUserActionDto infoUserActionDto) {
        if(infoUserActionDto.getCommand() == ChatInfoCommand.CONNECT ||
                infoUserActionDto.getCommand() == ChatInfoCommand.DISCONNECT) {
            ChatUserDto chatUserDto = infoUserActionDto.getChatUserDto();

            chatRoomDto.getUsers()
                    .stream()
                    .filter(user -> user.getAccountId().equals(chatUserDto.getAccountId()))
                    .findFirst()
                    .ifPresent(user -> {
                        user.setStatus(chatUserDto.getStatus());
                        user.setName(chatUserDto.getName());

                        Tools.runOnWithOutThreadFX(() -> updateRoom(this.chatRoomDto));
                    });
        }
    }

    @Override
    public void handlerInfo(ChatInfoMessageActionDto infoMessageActionDto) {
        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser();
        Tools.runOnWithOutThreadFX(() -> {
            for (ChatMessageDto chatMessageDto : infoMessageActionDto.getChatMessageDtoList()) {
                if(chatMessageDto.getRoomId().equals(chatRoomDto.getId())) {
                    if(infoMessageActionDto.getCommand() == ChatInfoCommand.HIDE_MESSAGES) {
//                        if(authorizedUser.isAccessOit()) {
//                            chatRoomMessagesVc.hideMessage(chatMessageDto); TODO: refactoring
//                        } else {
//                            chatRoomMessagesVc.removeMessage(chatMessageDto);
//                        }
                    } else if(infoMessageActionDto.getCommand() == ChatInfoCommand.DELETE_MESSAGES) {
                        chatRoomMessagesVc.removeMessage(chatMessageDto);
                    }
                }
            }
        });
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        handleTransportError(session, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;

        chatClient.subscribeRoom(this.chatRoomDto, this);

        ServerResponse<Page<ChatMessageDto>> messageByRoomId = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .getMessageByRoomId(chatRoomDto.getId());

        if(messageByRoomId.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(messageByRoomId.getExceptionMessage());
        } else {
            Map<Long, ChatMessageDto> messageMap = chatRoomMessagesVc.getMessages()
                    .stream()
                    .map(ChatMessageVc::getChatMessageDto)
                    .collect(Collectors.toMap(ChatMessageDto::getId, Function.identity()));

            messageByRoomId.getResponse()
                    .getData()
                    .stream()
                    .filter(message -> !messageMap.containsKey(message.getId()))
                    .sorted(Comparator.comparing(ChatMessageDto::getId))
                    .collect(Collectors.toList())
                    .forEach(this::loadedMessage);
        }

        chatRoomMessagesVc.connect(chatClient);
        chatRoomPreviewVc.connect(chatClient);
    }

    private void loadedMessage(ChatMessageDto chatMessageDto) {
        Long authId = UniformSystemEditorKitSingleton
                .getInstance()
                .getAuthentication()
                .getAuthorizedUser()
                .getId();

        if(!chatMessageDto.isRead() && !authId.equals(chatMessageDto.getFromAccount().getAccountId())) {
            chatRoomPreviewVc.receivedMessage(this, chatMessageDto);
        }

        chatRoomMessagesVc.addMessage(chatMessageDto);
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void close(ChatClient chatClient) {
        chatClient.unSubscribe(this);
        chatRoomMessagesVc.close(chatClient);
        chatRoomPreviewVc.close(chatClient);
    }

    @Override
    public void setSubscriptionId(String id) {
        this.subscriptionId = id;
    }

    @Override
    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    private void init() {
        updateRoom(this.chatRoomDto);

        this.chatRoomMessagesVc.setSendMessage(chatMessageDto -> {
            if(chatClient != null) {
                chatClient.send(this.chatRoomDto, chatMessageDto);
            }
        });
    }

    @Override
    public void updateRoom(ChatRoomDto chatRoomDto) {
        String title = null;
        ChatUserStatus status = null;

        if(chatRoomDto.getType() == ChatGroupType.SINGLE) {
            ChatUserDto chatUserDto = chatRoomDto.getUsers().get(0);

            if(chatUserDto != null) {
                title = chatUserDto.getName();
                status = chatUserDto.getStatus();
            }
        } else {
            title = chatRoomDto.getName();

            if(StringUtils.isEmpty(title)) {
                if(chatRoomDto.getUsers().isEmpty()) {
                    title = "[Пустая комната]";
                } else {
                    title = "[" +
                            chatRoomDto.getUsers()
                                    .stream()
                                    .map(ChatUserDto::getName)
                                    .collect(Collectors.joining(",")) +
                            "]";
                }
            }
        }

        chatRoomDto.setName(title);

        this.chatRoomPreviewVc.setTitle(title);
        this.chatRoomPreviewVc.setStatus(status);
        this.chatRoomMessagesVc.setTitle(title);
    }

    @Override
    public void handleFrame(StompHeaders headers, ChatMessageDto message) {
        this.chatRoomObserverList.forEach(osb -> osb.receivedMessage(this, message));

        if(isSelected()) {
            message.setRead(true);

            markAsReadMessage(message);
        }
    }

    @Override
    public ChatRoomPreviewVc getChatRoomPreviewVc() {
        return this.chatRoomPreviewVc;
    }

    @Override
    public ChatRoomMessagesVc getChatRoomMessagesVc() {
        return this.chatRoomMessagesVc;
    }

    @Override
    public ChatRoomDto getChatRoomDto() {
        return this.chatRoomDto;
    }

    @Override
    public void registerChatRoomObserver(ChatRoomObserver chatRoomObserver) {
        chatRoomObserverList.add(chatRoomObserver);
    }

    @Override
    public void deleteChatRoomObserver(ChatRoomObserver chatRoomObserver) {
        chatRoomObserverList.remove(chatRoomObserver);
    }

    @Override
    public void setSelected(boolean selected) {
        chatRoomPreviewVc.setSelected(selected);
        chatRoomMessagesVc.setSelected(selected);

        if(selected) {
            markAsReadMessage(chatRoomMessagesVc.getMessages()
                    .stream()
                    .map(ChatMessageVc::getChatMessageDto)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public boolean isSelected() {
        return chatRoomPreviewVc.isSelected();
    }

    private void markAsReadMessage(ChatMessageDto messages) {
       markAsReadMessage(Collections.singletonList(messages));
    }

    private void markAsReadMessage(List<ChatMessageDto> messages) {
        Long authId = UniformSystemEditorKitSingleton
                .getInstance()
                .getAuthentication()
                .getAuthorizedUser()
                .getId();

        List<Long> messageIdsNotRead = messages
                .stream()
                .filter(message -> !authId.equals(message.getFromAccount().getAccountId()))
                .filter(message -> !message.isRead()).map(ChatMessageDto::getId)
                .collect(Collectors.toList());

        if(!messageIdsNotRead.isEmpty()) {
            ServerResponse serverResponse = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .markAsReadMessage(new IdsDto(messageIdsNotRead));

            if (serverResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(serverResponse.getExceptionMessage());
            }
        }
    }
}
