package ru.kolaer.client.chat.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.chat.service.*;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolchat.*;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.plugins.UniformSystemPlugin;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;

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
    private final ChatInfoMessageActionHandler chatInfoMessageActionHandler;
    private final NotificationMessage notificationMessage;

    private final ObservableList<ChatRoomVc> chatRooms = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    private ChatRoomVc lastSelected;

    private BorderPane mainPane;
    private ChatRoomListVc chatRoomListVc;
    private ChatClient chatClient;
    private SplitPane splitPane;

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

        this.chatInfoMessageActionHandler = new ChatInfoMessageActionHandlerAbsctract() {
            @Override
            public void handlerInfo(ChatInfoMessageActionDto chatInfoDto) {
                ChatContentVcImpl.this.handlerInfo(chatInfoDto);
            }
        };
    }

    @Override
    public void initView(Consumer<ChatContentVc> viewVisit) {
        mainPane = new BorderPane();
        mainPane.getStylesheets().add(getClass().getResource("/chat.css").toString());

        splitPane = new SplitPane();

        chatRoomListVc.initView(chatRoomList -> splitPane.getItems().add(0, chatRoomList.getContent()));
        chatRoomListVc.setOnSelectRoom(chatRoomVc -> {
            if(lastSelected != null) {
                lastSelected.setSelected(false);

                if(splitPane.getItems().size() > 1) {
                    splitPane.getItems().remove(1);
                }
            }

            if(chatRoomVc != null) {
                lastSelected = chatRoomVc;
                lastSelected.setSelected(true);

                ChatRoomMessagesVc chatRoomMessagesVc = chatRoomVc.getChatRoomMessagesVc();
                if (!chatRoomMessagesVc.isViewInit()) {
                    chatRoomMessagesVc.initView(BaseView::empty);
                }

                splitPane.getItems().add(1, chatRoomMessagesVc.getContent());
            }
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
        chatClient.subscribeInfo(chatInfoMessageActionHandler);

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

                    chatRoomListVc.sorted();
            });

            ServerResponse<List<ChatUserDto>> onlineUser = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getOnlineUser();

            if(onlineUser.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(onlineUser.getExceptionMessage());
            } else {
                List<Long> accountsId = onlineUser.getResponse()
                        .stream()
                        .filter(user -> !user.getAccountId().equals(authorizedUser.getId()))
                        .map(ChatUserDto::getAccountId)
                        .collect(Collectors.toList());

                if(!accountsId.isEmpty()) {
                    ServerResponse<List<ChatRoomDto>> singleRooms = UniformSystemEditorKitSingleton.getInstance()
                            .getUSNetwork()
                            .getKolaerWebServer()
                            .getApplicationDataBase()
                            .getChatTable()
                            .createSingleRooms(new IdsDto(accountsId));

                    if (singleRooms.isServerError()) {
                        UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getNotification()
                                .showErrorNotify(singleRooms.getExceptionMessage());
                    }
                }
            }
        }

    }

    private List<ChatRoomVc> createChatRooms(List<ChatRoomDto> chatRoomDtos) {
        return chatRoomDtos.stream().map(this::createChatRoomVc).collect(Collectors.toList());
    }

    private ChatRoomVc createChatRoomVc(ChatRoomDto chatRoomDto) {
        ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatRoomDto);
        chatRoomVc.registerChatRoomObserver(notificationMessage);
        chatRoomVc.registerChatRoomObserver(chatRoomListVc);
        return chatRoomVc;
    }

    private void handlerInfo(ChatInfoRoomActionDto chatInfoDto) {
        Tools.runOnWithOutThreadFX(() -> {
            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser();

            ChatRoomDto chatRoomDto = chatInfoDto.getChatRoomDto();

            chatRoomDto.getUsers().removeIf(user -> user.getAccountId().equals(authorizedUser.getId()));

            if(chatInfoDto.getCommand() == ChatInfoCommand.CREATE_NEW_ROOM) {
                ChatRoomVc chatRoomVc = createChatRoomVc(chatRoomDto);
                chatRooms.add(chatRoomVc);
                chatRoomVc.connect(chatClient);
            } else if(chatInfoDto.getCommand() == ChatInfoCommand.QUIT_FROM_ROOM) {
                ChatRoomVc findChatRoomVc = chatRooms.stream()
                        .filter(chatRoomVc -> chatRoomVc.getChatRoomDto().getId().equals(chatRoomDto.getId()))
                        .findFirst()
                        .orElse(null);

                if(findChatRoomVc != null) {
                    if (authorizedUser.getId().equals(chatInfoDto.getFromAccount())) {
                        chatRooms.remove(findChatRoomVc);
                        findChatRoomVc.disconnect(chatClient);
                        findChatRoomVc.close(chatClient);
                    } else {
                        chatRoomDto.getUsers().removeIf(user -> user.getAccountId().equals(chatInfoDto.getFromAccount()));

                        findChatRoomVc.updateRoom(chatRoomDto);
                    }
                }
            }
        });

        chatRooms.forEach(chatRoomVc -> chatRoomVc.handlerInfo(chatInfoDto));
    }

    private void handlerInfo(ChatInfoUserActionDto infoUserActionDto) {
        chatRooms.forEach(chatRoomVc -> chatRoomVc.handlerInfo(infoUserActionDto));
    }

    private void handlerInfo(ChatInfoMessageActionDto chatInfoMessageActionDto) {
        chatRooms.forEach(chatRoomVc -> chatRoomVc.handlerInfo(chatInfoMessageActionDto));
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        chatRoomListVc.disconnect(chatClient);
    }

    @Override
    public void close(ChatClient chatClient) {
        Tools.runOnWithOutThreadFX(() -> {
            chatRooms.forEach(room -> room.close(chatClient));
            chatRooms.clear();

            if(splitPane.getItems().size() > 1) {
                splitPane.getItems().remove(1);
            }
        });

    }

    @Override
    public void showChatRoom(ChatRoomVc chatRoomVc) {
        chatRoomListVc.setSelectRoom(chatRoomVc);
    }
}
