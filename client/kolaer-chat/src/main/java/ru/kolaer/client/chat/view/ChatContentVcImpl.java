package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.chat.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 05.02.2018.
 */
@Slf4j
public class ChatContentVcImpl implements ChatContentVc {
    private final ChatInfoUserActionHandler chatInfoUserActionHandler;
    private final ChatInfoRoomActionHandler chatInfoRoomActionHandler;

    private BorderPane mainPane;
    private ChatRoomListVc chatRoomListVc;
    private ChatRoomVc selectedRoom;
    private ChatClient chatClient;

    public ChatContentVcImpl() {
        this.chatRoomListVc = new ChatRoomListVcImpl();

        this.chatInfoUserActionHandler = new ChatInfoUserActionHandlerAbsctract() {
            @Override
            public void handlerInfo(ChatInfoUserActionDto chatInfoDto) {
                chatRoomListVc.handlerInfo(chatInfoDto);
            }
        };

        this.chatInfoRoomActionHandler = new ChatInfoRoomActionHandlerAbsctract() {
            @Override
            public void handlerInfo(ChatInfoRoomActionDto chatInfoDto) {
                chatRoomListVc.handlerInfo(chatInfoDto);
            }
        };
    }

    @Override
    public void initView(Consumer<ChatContentVc> viewVisit) {
        mainPane = new BorderPane();

        SplitPane splitPane = new SplitPane();

        chatRoomListVc.initView(chatRoomList -> splitPane.getItems().add(chatRoomList.getContent()));
        chatRoomListVc.setOnSelectRoom(selected -> {
            selectedRoom = selected;
        });

        mainPane.setCenter(splitPane);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;

        chatRoomListVc.connect(chatClient);

        chatClient.subscribeInfo(chatInfoRoomActionHandler);
        chatClient.subscribeInfo(chatInfoUserActionHandler);

        ServerResponse<List<ChatRoomDto>> rooms = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .getRooms();

        if(rooms.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(rooms.getExceptionMessage());
        } else {
            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser();

            for (ChatRoomDto chatRoomDto : rooms.getResponse()) {
                Optional.ofNullable(chatRoomDto.getUsers()).orElse(Collections.emptyList())
                        .removeIf(user -> user.getAccountId().equals(authorizedUser.getId()));
            }

            chatRoomListVc.addChatRoomDto(rooms.getResponse());
        }
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        chatRoomListVc.disconnect(chatClient);
    }
}
