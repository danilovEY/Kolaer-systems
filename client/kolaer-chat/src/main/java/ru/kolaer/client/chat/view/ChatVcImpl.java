package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.UserListObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatVcImpl implements ChatVc, UserListObserver {
    private final Map<String, ChatRoomVc> groupDtoMap = new HashMap<>();
    private String subInfoId;
    private BorderPane mainPane;
    private TabPane tabPane;
    private ChatClient chatClient;

    @Override
    public void initView(Consumer<ChatVc> viewVisit) {
        mainPane = new BorderPane();

        tabPane = new TabPane();

        mainPane.setCenter(tabPane);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;

        chatClient.subscribeInfo(this);

        ServerResponse<List<ChatGroupDto>> activeGroup = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .getActiveGroup();

        if(activeGroup.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(activeGroup.getExceptionMessage());
        } else {
            activeGroup.getResponse()
                    .forEach(this::createRoom);
        }
    }

    private void createRoom(ChatGroupDto chatGroupDto) {
        Tools.runOnWithOutThreadFX(() -> {
            if(groupDtoMap.containsKey(chatGroupDto.getRoomId())) {
                ChatRoomVc chatRoomVc = groupDtoMap.get(chatGroupDto.getRoomId());
                tabPane.getSelectionModel().select(chatRoomVc.getContent());
            } else {
                ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatGroupDto);
                chatRoomVc.initView(initRoom -> {
                    tabPane.getTabs().add(initRoom.getContent());
                    if(chatClient.isConnect()) {
                        chatRoomVc.connect(chatClient);
                    }
                });
                groupDtoMap.put(chatGroupDto.getRoomId(), chatRoomVc);
            }
        });
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        for (ChatRoomVc chatRoomVc : groupDtoMap.values()) {
            chatRoomVc.disconnect(chatClient);
        }
    }

    @Override
    public void addChatRoom(ChatRoomVc chatRoomVc) {
        this.tabPane.getTabs().add(chatRoomVc.getContent());
    }

    @Override
    public void handleFrame(StompHeaders headers, ChatInfoDto info) {
        if(info.getCommand() == ChatInfoCommand.CONNECT) {
            ServerResponse<ChatUserDto> activeByIdAccountResponse = UniformSystemEditorKitSingleton
                    .getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getActiveByIdAccount(info.getAccountId());

            if(activeByIdAccountResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(activeByIdAccountResponse.getExceptionMessage());
            } else {
                Tools.runOnWithOutThreadFX(() -> {
                    for (ChatRoomVc chatRoomVc : groupDtoMap.values()) {
                        chatRoomVc.connectUser(activeByIdAccountResponse.getResponse());
                    }
                });
            }
        } else if(info.getCommand() == ChatInfoCommand.DISCONNECT) {
            Tools.runOnWithOutThreadFX(() -> {
                for (ChatRoomVc chatRoomVc : groupDtoMap.values()) {
                    chatRoomVc.disconnectUser(info.getAccountId());
                }
            });
        } else if(info.getCommand() == ChatInfoCommand.CREATE_NEW_ROOM) {
            ServerResponse<ChatGroupDto> groupDtoServerResponse = UniformSystemEditorKitSingleton
                    .getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getGroupByRoomId(info.getData());

            if(groupDtoServerResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(groupDtoServerResponse.getExceptionMessage());
            } else {
                Tools.runOnWithOutThreadFX(() -> {
                   createRoom(groupDtoServerResponse.getResponse());
                });
            }
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public void setSubscriptionId(String id) {
        subInfoId = id;
    }

    @Override
    public String getSubscriptionId() {
        return subInfoId;
    }
}
