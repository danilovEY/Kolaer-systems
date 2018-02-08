package ru.kolaer.client.chat.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoRoomActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 05.02.2018.
 */
@Slf4j
public class ChatContentVcImpl implements ChatContentVc {
    private final ChatInfoUserActionHandler chatInfoUserActionHandler;
    private final ChatInfoRoomActionHandler chatInfoRoomActionHandler;
    private final NotificationMessage notificationMessage;

    private final ObservableList<ChatRoomVc> chatRooms = FXCollections.observableArrayList();

    private ChatRoomVc lastSelected;

    private BorderPane mainPane;
    private ChatRoomListVc chatRoomListVc;
    private ChatClient chatClient;

    public ChatContentVcImpl(UniformSystemPlugin uniformSystemPlugin) {
        this.chatRoomListVc = new ChatRoomListVcImpl(chatRooms);
        this.notificationMessage = new NotificationMessagePopup(this, uniformSystemPlugin);

        this.chatInfoUserActionHandler = new ChatInfoUserActionHandlerAbsctract() {
            @Override
            public void handlerInfo(ChatInfoUserActionDto chatInfoDto) {
                ChatContentVcImpl.this.handlerInfo(chatInfoDto);
            }
        };

        this.chatInfoRoomActionHandler = new ChatInfoRoomActionHandlerAbsctract() {
            @Override
            public void handlerInfo(ChatInfoRoomActionDto chatInfoDto) {
                ChatContentVcImpl.this.handlerInfo(chatInfoDto);
            }
        };
    }

    @Override
    public void initView(Consumer<ChatContentVc> viewVisit) {
        mainPane = new BorderPane();
        mainPane.getStylesheets().add(getClass().getResource("/chat.css").toString());

        SplitPane splitPane = new SplitPane();

        chatRoomListVc.initView(chatRoomList -> splitPane.getItems().add(0, chatRoomList.getContent()));
        chatRoomListVc.setOnSelectRoom(chatRoomVc -> {
            if(lastSelected != null) {
                lastSelected.setSelected(false);
            }

            lastSelected = chatRoomVc;
            lastSelected.setSelected(true);

            ChatRoomMessagesVc chatRoomMessagesVc = chatRoomVc.getChatRoomMessagesVc();
            if(!chatRoomMessagesVc.isViewInit()) {
                chatRoomMessagesVc.initView(BaseView::empty);
            }

            splitPane.getItems().set(1, chatRoomMessagesVc.getContent());
        });

        splitPane.getItems().add(1, new Pane());

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

            Map<Long, ChatRoomDto> chatRoomMap = chatRooms
                    .stream()
                    .map(ChatRoomVc::getChatRoomDto)
                    .collect(Collectors.toMap(ChatRoomDto::getId, Function.identity()));

            Tools.runOnWithOutThreadFX(() -> {
                rooms.getResponse()
                        .stream()
                        .filter(chatRoom -> !chatRoomMap.containsKey(chatRoom.getId()))
                        .map(this::createChatRoomVc)
                        .forEach(chatRooms::add);

                    chatRooms.forEach(chatRoom -> chatRoom.connect(chatClient));
            });

//            ServerResponse<List<ChatUserDto>> onlineUser = UniformSystemEditorKitSingleton.getInstance()
//                    .getUSNetwork()
//                    .getKolaerWebServer()
//                    .getApplicationDataBase()
//                    .getChatTable()
//                    .getOnlineUser();
//
//            if(onlineUser.isServerError()) {
//                UniformSystemEditorKitSingleton.getInstance()
//                        .getUISystemUS()
//                        .getNotification()
//                        .showErrorNotify(onlineUser.getExceptionMessage());
//            } else {
//                List<Long> accountsId = onlineUser.getResponse()
//                        .stream()
//                        .filter(user -> !user.getAccountId().equals(authorizedUser.getId()))
//                        .map(ChatUserDto::getAccountId)
//                        .collect(Collectors.toList());
//
//                if(!accountsId.isEmpty()) {
//                    ServerResponse<List<ChatRoomDto>> singleRooms = UniformSystemEditorKitSingleton.getInstance()
//                            .getUSNetwork()
//                            .getKolaerWebServer()
//                            .getApplicationDataBase()
//                            .getChatTable()
//                            .createSingleRooms(new IdsDto(accountsId));
//
//                    if (singleRooms.isServerError()) {
//                        UniformSystemEditorKitSingleton.getInstance()
//                                .getUISystemUS()
//                                .getNotification()
//                                .showErrorNotify(singleRooms.getExceptionMessage());
//                    }
//                }
//            }
        }

    }

    private List<ChatRoomVc> createChatRooms(List<ChatRoomDto> chatRoomDtos) {
        return chatRoomDtos.stream().map(this::createChatRoomVc).collect(Collectors.toList());
    }

    private ChatRoomVc createChatRoomVc(ChatRoomDto chatRoomDto) {
        ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatRoomDto);
        chatRoomVc.registerChatRoomObserver(notificationMessage);
        return chatRoomVc;
    }

    private void handlerInfo(ChatInfoRoomActionDto chatInfoDto) {
        Tools.runOnWithOutThreadFX(() -> {
            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser();

            chatInfoDto.getChatRoomDto()
                    .getUsers()
                    .removeIf(user -> user.getAccountId().equals(authorizedUser.getId()));

            if(chatInfoDto.getCommand() == ChatInfoCommand.CREATE_NEW_ROOM) {
                chatRooms.add(createChatRoomVc(chatInfoDto.getChatRoomDto()));
            }
        });

        chatRooms.forEach(chatRoomVc -> chatRoomVc.handlerInfo(chatInfoDto));
    }

    private void handlerInfo(ChatInfoUserActionDto infoUserActionDto) {
        chatRooms.forEach(chatRoomVc -> chatRoomVc.handlerInfo(infoUserActionDto));
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        chatRoomListVc.disconnect(chatClient);
    }

    @Override
    public void showChatRoom(ChatRoomVc chatRoomVc) {
        chatRoomListVc.setSelectRoom(chatRoomVc);
    }
}
