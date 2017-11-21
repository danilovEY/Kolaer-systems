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
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatInfoHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatVcImpl implements ChatVc {
    private final Map<String, ChatRoomVc> groupDtoMap = new HashMap<>();
    private BorderPane mainPane;
    private TabPane tabPane;

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
        ServerResponse<List<ChatGroupDto>> activeGroup = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .getActiveGroup();

        log.info(activeGroup.toString());

        if(activeGroup.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(activeGroup.getExceptionMessage());
        } else {
            List<ChatGroupDto> response = activeGroup.getResponse();

            //AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
            //        .getAuthentication()
            //        .getAuthorizedUser();

            for(ChatGroupDto chatGroupDto : response) {
                Tools.runOnWithOutThreadFX(() -> {
                    ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatGroupDto);
                    chatRoomVc.initView(initRoom -> tabPane.getTabs().add(initRoom.getContent()));
                    if(chatClient.isConnect()) {
                        chatRoomVc.connect(chatClient);
                    }
                    chatClient.registerObserver(chatRoomVc);
                    groupDtoMap.put(chatGroupDto.getName(), chatRoomVc);
                });
            }
        }

        chatClient.subscribeInfo(new ChatInfoHandler() {
            @Override
            public void handleFrame(StompHeaders headers, ChatInfoDto info) {
                log.info(info.toString());
                ServerResponse<List<ChatGroupDto>> activeGroup = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .getActiveGroup();

                log.info(activeGroup.toString());

                if(activeGroup.isServerError()) {
                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getNotification()
                            .showErrorNotify(activeGroup.getExceptionMessage());
                } else {
                    for (ChatGroupDto chatGroupDto : activeGroup.getResponse()) {
                        if(groupDtoMap.containsKey(chatGroupDto.getName())) {
                            ChatRoomVc chatRoomVc = groupDtoMap.get(chatGroupDto.getName());
                            Tools.runOnWithOutThreadFX(() -> {
                                chatRoomVc.getUserListVc().setUsers(chatGroupDto.getUsers());
                            });
                        }
                    }
                }
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {

            }
        });
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void addChatRoom(ChatRoomVc chatRoomVc) {
        this.tabPane.getTabs().add(chatRoomVc.getContent());
    }

}
