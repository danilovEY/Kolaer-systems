package ru.kolaer.client.chat.view;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatRoomObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class TabChatRoomVcImpl implements TabChatRoomVc {
    private final List<ChatRoomObserver> observers = new ArrayList<>();
    private final ChatRoomDto chatRoomDto;

    private final ChatMessageContentVc chatMessageContentVc;
    private final UserListVc userListVc;
    private final String title;

    private String subscriptionId;

    private Tab mainTab;

    private ChatClient chatClient;

    public TabChatRoomVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
        this.chatMessageContentVc = new ChatMessageContentVcImpl(chatRoomDto);
        this.userListVc = new UserListVcImpl(chatRoomDto);

        this.title = generateTitle(chatRoomDto);
        chatRoomDto.setName(title);
    }

    @Override
    public void initView(Consumer<TabChatRoomVc> viewVisit) {

        mainTab = new Tab();
        mainTab.setText(title);
        mainTab.setOnClosed(e -> {
            //if(chatClient != null) {
            //    disconnect(chatClient);
            //}
        });

        mainTab.setOnSelectionChanged(e -> {
            if(mainTab.isSelected()) {
                mainTab.setText(title);
            }
        });

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.3);

        userListVc.initView(initUserList -> {
            splitPane.getItems().add(initUserList.getContent());
            initUserList.setUsers(chatRoomDto.getUsers());
            initUserList.setOnCreateMessageToUser(this::createMessageToUser);
        });

        chatMessageContentVc.initView(initMessage -> {
            splitPane.getItems().add(initMessage.getContent());
            initMessage.setOnSendMessage(this::sendMessage);
        });

        mainTab.setContent(splitPane);

        viewVisit.accept(this);
    }

    private String generateTitle(ChatRoomDto chatRoomDto) {
        if(chatRoomDto == null) {
            return "Неизвестный";
        }

//        if (CollectionUtils.isEmpty(chatRoomDto.getUsers()) || chatRoomDto.getType() != ChatGroupType.PRIVATE) {
//            return StringUtils.hasText(chatRoomDto.getName()) ? chatRoomDto.getName() : chatRoomDto.getRoomId();
//        } else {
//            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
//                    .getAuthentication()
//                    .getAuthorizedUser();
//            return chatRoomDto.getUsers()
//                    .stream()
//                    .filter(user -> !user.getAccountId().equals(authorizedUser.getId()))
//                    .map(ChatUserDto::getAccount)
//                    .map(this::accountsToChatGroupName)
//                    .collect(Collectors.joining(","));
//        }
        return null;
    }

    private String accountsToChatGroupName(AccountDto accountDto) {
        String username = accountDto.getChatName();
        if(StringUtils.isEmpty(username)) {
            EmployeeDto employee = accountDto.getEmployee();
            if (employee == null) {
                username = accountDto.getUsername();
            } else {
                username = employee.getInitials();
            }
        }
        return "[" + username + "]";
    }

    @Override
    public Tab getContent() {
        return mainTab;
    }

    @Override
    public void setSubscriptionId(String id) {
        this.subscriptionId = id;
    }

    @Override
    public String getSubscriptionId() {
        return subscriptionId;
    }

    @Override
    public void addObserver(ChatRoomObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ChatRoomObserver observer) {
        observers.remove(observer);
    }

    public ChatRoomDto getChatRoomDto() {
        return chatRoomDto;
    }

    @Override
    public void addMessages(List<ChatMessageDto> messages) {
        messages.forEach(chatMessageContentVc::addMessage);
    }

    @Override
    public void removeMessages(List<Long> messages) {
        if(messages == null || messages.isEmpty()) {
            return;
        }

        List<ChatMessageDto> removedMessages = chatMessageContentVc.getMessages()
                .stream()
                .filter(message -> messages.contains(message.getId()))
                .collect(Collectors.toList());

        Tools.runOnWithOutThreadFX(() -> chatMessageContentVc.removeMessages(removedMessages));
    }

    private void createMessageToUser(List<ChatUserDto> chatUserDto) {
        for (ChatRoomObserver observer : observers) {
            observer.createMessageToUser(chatRoomDto, chatUserDto);
        }
    }

    private void sendMessage(ChatMessageDto chatMessageDto) {
        chatClient.send(chatRoomDto, chatMessageDto);

        for (ChatRoomObserver observer : observers) {
            observer.sendMessage(chatRoomDto, chatMessageDto);
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
    public void handleFrame(StompHeaders headers, ChatMessageDto message) {
        Tools.runOnWithOutThreadFX(() -> {
            chatMessageContentVc.addMessage(message);
            if(message.getType() != ChatMessageType.SERVER_INFO) {
                if (isViewInit() && !mainTab.isSelected()) {
                    mainTab.setText(mainTab.getText() + " [+1]");
                }
            }
        });

        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        if(message.getType() != ChatMessageType.SERVER_INFO &&
                authorizedUser != null && authorizedUser.getId() != null &&
                !authorizedUser.getId()
                        .equals(Optional.ofNullable(message.getFromAccount())
                                .map(AccountDto::getId)
                                .orElse(-1L))) {
            for (ChatRoomObserver observer : observers) {
                observer.getMessage(chatRoomDto, message);
            }
        }
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;
        chatClient.subscribeRoom(chatRoomDto, this);

        if(chatRoomDto.getType() != ChatGroupType.MAIN) {
            ServerResponse<ChatRoomDto> groupDtoServerResponse = UniformSystemEditorKitSingleton
                    .getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .getRoomById(chatRoomDto.getId());

            if (!groupDtoServerResponse.isServerError() && groupDtoServerResponse.getResponse() == null) {
                createGroup();
            } else {
                log.error("{}", groupDtoServerResponse.getExceptionMessage());
            }
        }
    }

    private void createGroup() {
        IdsDto idsAccounts = new IdsDto(chatRoomDto.getUsers()
                .stream()
                .map(ChatUserDto::getAccountId)
                .collect(Collectors.toList()));

        ServerResponse<ChatRoomDto> groupDtoServerResponse = UniformSystemEditorKitSingleton
                .getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .createPrivateRoom(idsAccounts, chatRoomDto.getName());

        if(groupDtoServerResponse.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(groupDtoServerResponse.getExceptionMessage());
        }
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        chatClient.unSubscribe(this);
    }

    @Override
    public void connectUser(ChatUserDto chatUserDto) {
        if(chatRoomDto.getType() == ChatGroupType.MAIN) {
            chatRoomDto.getUsers().add(chatUserDto);
        }

        if(isViewInit()) {
            Tools.runOnWithOutThreadFX(() -> {
                if(isViewInit()) {
                    userListVc.addUser(chatUserDto);
                }

                for (ChatRoomObserver observer : observers) {
                    //observer.connectUser(chatRoomDto, chatUserDto);
                }
            });
        }
    }

    @Override
    public void disconnectUser(Long accountId) {
        Optional<ChatUserDto> chatUserDtoOptional = chatRoomDto.getUsers()
                .stream()
                .filter(user -> user.getAccountId()
                .equals(accountId))
                .findFirst();

        if(chatUserDtoOptional.isPresent()) {
            ChatUserDto chatUserDto = chatUserDtoOptional.get();

            if(chatRoomDto.getType() == ChatGroupType.MAIN) {
                chatRoomDto.getUsers().remove(chatUserDto);
            }

            if(isViewInit()) {
                Tools.runOnWithOutThreadFX(() -> userListVc.removeUser(chatUserDto));
            }

            for (ChatRoomObserver observer : observers) {
                //observer.disconnectUser(chatRoomDto, chatUserDto);
            }
        }
    }
}
