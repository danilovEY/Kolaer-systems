package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
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

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class TabChatVcImpl implements TabChatVc, ChatRoomObserver {
    private final Map<Long, TabChatRoomVc> groupDtoMap = new HashMap<>();
    private final UniformSystemPlugin uniformSystemPlugin;
    private String subInfoId;
    private BorderPane mainPane;
    private TabPane tabPane;
    private ChatClient chatClient;
    private NotificationMessage notificationMessagePane;
    private Label labelInfo;

    public TabChatVcImpl(UniformSystemPlugin uniformSystemPlugin) {
        this.uniformSystemPlugin = uniformSystemPlugin;
    }

    @Override
    public void initView(Consumer<TabChatVc> viewVisit) {
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

        for (TabChatRoomVc tabChatRoomVc : groupDtoMap.values()) {
            tabChatRoomVc.connect(chatClient);
        }

        Tools.runOnWithOutThreadFX(() -> labelInfo.setText("Загрузка комнат..."));

        ServerResponse<List<ChatRoomDto>> activeGroup = UniformSystemEditorKitSingleton.getInstance()
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

    private boolean filterChatGroup(ChatRoomDto chatRoomDto) {
        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        return chatRoomDto.getType() == ChatGroupType.MAIN ||
                (chatRoomDto.getUserCreated() != null && chatRoomDto.getUserCreated()
                        .getId().equals(authorizedUser.getId())) ||
                (chatRoomDto.getUsers() != null &&
                        chatRoomDto.getUsers()
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

    private TabChatRoomVc createRoom(ChatRoomDto chatRoomDto) {
        if(groupDtoMap.containsKey(chatRoomDto.getId())) {
            return groupDtoMap.get(chatRoomDto.getId());
        }

        TabChatRoomVc tabChatRoomVc = new TabChatRoomVcImpl(chatRoomDto);
        groupDtoMap.put(chatRoomDto.getId(), tabChatRoomVc);
        tabChatRoomVc.addObserver(this);
        tabChatRoomVc.addObserver(notificationMessagePane);

        if(chatClient.isConnect()) {
            tabChatRoomVc.connect(chatClient);
        }

        if(chatRoomDto.getType() == ChatGroupType.MAIN) {
            Tools.runOnWithOutThreadFX(() -> showChatRoom(tabChatRoomVc, false));
        }

        return tabChatRoomVc;
    }

    private void initRoom(TabChatRoomVc tabChatRoomVc) {
        if(tabChatRoomVc == null) {
            return;
        }

        tabChatRoomVc.initView((TabChatRoomVc chatRoom) -> {
            ServerResponse<Page<ChatMessageDto>> messageByRoomId = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getMessageByRoomId(chatRoom.getChatRoomDto().getId());

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
        for (TabChatRoomVc tabChatRoomVc : groupDtoMap.values()) {
            tabChatRoomVc.disconnect(chatClient);
        }
    }

    @Override
    public TabChatRoomVc showChatRoom(TabChatRoomVc tabChatRoomVc, boolean focus) {
        if(!tabChatRoomVc.isViewInit()) {
            initRoom(tabChatRoomVc);
        }

        if(!roomIsShow(tabChatRoomVc)) {
            tabPane.getTabs().add(tabChatRoomVc.getContent());
        }

        if(focus) {
            tabPane.getSelectionModel().select(tabChatRoomVc.getContent());
        }

        return tabChatRoomVc;
    }

    @Override
    public TabChatRoomVc getChatRoom(ChatRoomDto chatRoomDto) {
        return groupDtoMap.get(chatRoomDto.getId());
    }

    @Override
    public boolean roomIsShow(TabChatRoomVc tabChatRoomVc) {
        return tabPane.getTabs().contains(tabChatRoomVc.getContent());
    }

    @Override
    public boolean roomIsFocus(TabChatRoomVc tabChatRoomVc) {
        return tabPane.getSelectionModel().isSelected(tabPane.getTabs().indexOf(tabChatRoomVc.getContent()));
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
                    for (TabChatRoomVc tabChatRoomVc : groupDtoMap.values()) {
                        tabChatRoomVc.connectUser(activeByIdAccountResponse.getResponse());
                    }
                });
            }
        } else if(info.getCommand() == ChatInfoCommand.DISCONNECT) {
            Tools.runOnWithOutThreadFX(() -> {
                for (TabChatRoomVc tabChatRoomVc : groupDtoMap.values()) {
                    tabChatRoomVc.disconnectUser(info.getAccountId());
                }
            });
        } else if(info.getCommand() == ChatInfoCommand.CREATE_NEW_ROOM) {
            ServerResponse<ChatRoomDto> groupDtoServerResponse = UniformSystemEditorKitSingleton
                    .getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getGroupByRoomId(((ChatInfoCreateNewRoomDto)info).getData().getId());

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
//            if(StringUtils.isEmpty(info.getData())) {
//                return;
//            }
//
//            List<Long> messageIds = Stream.of(info.getData().split(","))
//                    .map(Long::valueOf)
//                    .collect(Collectors.toList());
//
//            for (TabChatRoomVc tabChatRoomVc : groupDtoMap.values()) {
//                tabChatRoomVc.removeMessages(messageIds);
//            }

        }
    }

    @Override
    public void handlerInfo(ChatInfoDto chatInfoDto) {

    }

    @Override
    public void handlerNewRoom(ChatInfoCreateNewRoomDto chatInfoCreateNewRoomDto) {

    }

    @Override
    public void handlerUserAction(ChatInfoUserActionDto chatInfoUserActionDto) {

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
    public void getMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto) {
        TabChatRoomVc tabChatRoomVc = groupDtoMap.get(chatRoomDto.getId());
        if(tabChatRoomVc != null) {
            Tools.runOnWithOutThreadFX(() -> {
                showChatRoom(tabChatRoomVc, false);
            });
        }
    }

    @Override
    public void createMessageToUser(ChatRoomDto chatRoomDto, List<ChatUserDto> chatUserDtos) {
        IdsDto idsAccounts = new IdsDto(chatUserDtos.stream()
                .map(ChatUserDto::getAccountId)
                .collect(Collectors.toList()));

        ServerResponse<ChatRoomDto> groupDtoServerResponse = UniformSystemEditorKitSingleton
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
