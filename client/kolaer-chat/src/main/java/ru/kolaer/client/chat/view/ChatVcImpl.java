package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatRoomObserver;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatVcImpl implements ChatVc, ChatRoomObserver {
    private final Map<String, ChatRoomVc> groupDtoMap = new HashMap<>();
    private final UniformSystemPlugin uniformSystemPlugin;
    private String subInfoId;
    private BorderPane mainPane;
    private TabPane tabPane;
    private ChatClient chatClient;
    private NotificationMessage notificationMessagePane;
    private Label labelInfo;

    public ChatVcImpl(UniformSystemPlugin uniformSystemPlugin) {
        this.uniformSystemPlugin = uniformSystemPlugin;
    }

    @Override
    public void initView(Consumer<ChatVc> viewVisit) {
        mainPane = new BorderPane();
        mainPane.getStylesheets().add(getClass().getResource("/chat.css").toString());

        tabPane = new TabPane();

        labelInfo = new Label("Вы не авторизовались");

        mainPane.setCenter(labelInfo);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        Tools.runOnWithOutThreadFX(() -> labelInfo.setText("Загрузка комнат..."));

        this.chatClient = chatClient;

        chatClient.subscribeInfo(this);

        this.notificationMessagePane = new NotificationMessagePopup(this, uniformSystemPlugin);

        if(!groupDtoMap.isEmpty()) {
            Tools.runOnWithOutThreadFX(() -> mainPane.setCenter(tabPane));
        }

        for (ChatRoomVc chatRoomVc : groupDtoMap.values()) {
            chatRoomVc.connect(chatClient);
        }

        Tools.runOnWithOutThreadFX(() -> labelInfo.setText("Загрузка комнат..."));

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
            Tools.runOnWithOutThreadFX(() -> labelInfo.setText("Инициализация комнат..."));

            activeGroup.getResponse()
                    .stream()
                    .filter(this::filterChatGroup)
                    .forEach(this::createRoom);

            Tools.runOnWithOutThreadFX(() -> mainPane.setCenter(tabPane));
        }
    }

    private boolean filterChatGroup(ChatGroupDto chatGroupDto) {
        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        return chatGroupDto.getType() == ChatGroupType.MAIN ||
                (chatGroupDto.getUserCreated() != null && chatGroupDto.getUserCreated()
                        .getId().equals(authorizedUser.getId())) ||
                (chatGroupDto.getUsers() != null &&
                        chatGroupDto.getUsers()
                                .stream()
                                .anyMatch(chatUserDto -> chatUserDto.getAccountId().equals(authorizedUser.getId())));
    }

    @Override
    public void setSubscriptionId(String id) {
        subInfoId = id;
    }

    @Override
    public String getSubscriptionId() {
        return subInfoId;
    }

    private ChatRoomVc createRoom(ChatGroupDto chatGroupDto) {
        if(groupDtoMap.containsKey(chatGroupDto.getRoomId())) {
            return groupDtoMap.get(chatGroupDto.getRoomId());
        }

        ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatGroupDto);
        groupDtoMap.put(chatGroupDto.getRoomId(), chatRoomVc);
        chatRoomVc.addObserver(this);
        chatRoomVc.addObserver(notificationMessagePane);

        if(chatClient.isConnect()) {
            chatRoomVc.connect(chatClient);
        }

        if(chatGroupDto.getType() == ChatGroupType.MAIN) {
            Tools.runOnWithOutThreadFX(() -> showChatRoom(chatRoomVc, false));
        }

        return chatRoomVc;
    }

    private void initRoom(ChatRoomVc chatRoomVc) {
        if(chatRoomVc == null) {
            return;
        }

        chatRoomVc.initView((ChatRoomVc chatRoom) -> {
            ServerResponse<Page<ChatMessageDto>> messageByRoomId = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getMessageByRoomId(chatRoom.getChatGroupDto().getRoomId());

            if(messageByRoomId.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(messageByRoomId.getExceptionMessage());
            } else {
                Page<ChatMessageDto> response = messageByRoomId.getResponse();
                List<ChatMessageDto> data = response.getData();
                data.sort(Comparator.comparingLong(ChatMessageDto::getId));
                chatRoom.addMessages(data);
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
    public ChatRoomVc showChatRoom(ChatRoomVc chatRoomVc, boolean focus) {
        if(!chatRoomVc.isViewInit()) {
            initRoom(chatRoomVc);
        }

        if(!roomIsShow(chatRoomVc)) {
            tabPane.getTabs().add(chatRoomVc.getContent());
        }

        if(focus) {
            tabPane.getSelectionModel().select(chatRoomVc.getContent());
        }

        return chatRoomVc;
    }

    @Override
    public ChatRoomVc getChatRoom(ChatGroupDto chatGroupDto) {
        return groupDtoMap.get(chatGroupDto.getRoomId());
    }

    @Override
    public boolean roomIsShow(ChatRoomVc chatRoomVc) {
        return tabPane.getTabs().contains(chatRoomVc.getContent());
    }

    @Override
    public boolean roomIsFocus(ChatRoomVc chatRoomVc) {
        return tabPane.getSelectionModel().isSelected(tabPane.getTabs().indexOf(chatRoomVc.getContent()));
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
        } else if(info.getCommand() == ChatInfoCommand.HIDE_MESSAGES) {
            log.info(info.toString());
            if(StringUtils.isEmpty(info.getData())) {
                return;
            }

            List<Long> messageIds = Stream.of(info.getData().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            for (ChatRoomVc chatRoomVc : groupDtoMap.values()) {
                chatRoomVc.removeMessages(messageIds);
            }

        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }


    @Override
    public void getMessage(ChatGroupDto chatGroupDto, ChatMessageDto chatMessageDto) {
        ChatRoomVc chatRoomVc = groupDtoMap.get(chatGroupDto.getRoomId());
        if(chatRoomVc != null) {
            Tools.runOnWithOutThreadFX(() -> {
                showChatRoom(chatRoomVc, false);
            });
        }
    }

    @Override
    public void createMessageToUser(ChatGroupDto chatGroupDto, List<ChatUserDto> chatUserDtos) {
        IdsDto idsAccounts = new IdsDto(chatUserDtos.stream()
                .map(ChatUserDto::getAccountId)
                .collect(Collectors.toList()));

        ServerResponse<ChatGroupDto> groupDtoServerResponse = UniformSystemEditorKitSingleton
                .getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .createPrivateGroup(idsAccounts, null);

        if(groupDtoServerResponse.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(groupDtoServerResponse.getExceptionMessage());
        } else {
            Tools.runOnWithOutThreadFX(() -> {
                showChatRoom(createRoom(groupDtoServerResponse.getResponse()), true);
            });
        }
    }
}
