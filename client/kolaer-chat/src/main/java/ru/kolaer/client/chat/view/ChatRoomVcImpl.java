package ru.kolaer.client.chat.view;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
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
public class ChatRoomVcImpl implements ChatRoomVc {
    private final List<ChatRoomObserver> observers = new ArrayList<>();
    private final ChatGroupDto chatGroupDto;

    private final ChatMessageContentVc chatMessageContentVc;
    private final UserListVc userListVc;

    private String subscriptionId;

    private Tab mainTab;

    private ChatClient chatClient;

    public ChatRoomVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
        this.chatMessageContentVc = new ChatMessageContentVcImpl(chatGroupDto);
        this.userListVc = new UserListVcImpl(chatGroupDto);
    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        mainTab = new Tab();
        mainTab.setText(generateTitle(chatGroupDto));
        mainTab.setOnClosed(e -> {
            //if(chatClient != null) {
            //    disconnect(chatClient);
            //}
        });
        /*mainTab.setOnSelectionChanged(e -> {
            if(mainTab.isSelected()) {
                mainTab.setText(chatGroupDto.getName());
            }
        });*/

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.3);

        userListVc.initView(initUserList -> {
            splitPane.getItems().add(initUserList.getContent());
            initUserList.setUsers(chatGroupDto.getUsers());
            initUserList.setOnCreateMessageToUser(this::createMessageToUser);
        });

        chatMessageContentVc.initView(initMessage -> {
            splitPane.getItems().add(initMessage.getContent());
            initMessage.setOnSendMessage(this::sendMessage);
        });

        mainTab.setContent(splitPane);

        viewVisit.accept(this);
    }

    private String generateTitle(ChatGroupDto chatGroupDto) {
        if(chatGroupDto == null) {
            return "Неизвестный";
        }

        if (CollectionUtils.isEmpty(chatGroupDto.getUsers()) || chatGroupDto.getType() != ChatGroupType.PRIVATE) {
            return StringUtils.hasText(chatGroupDto.getName()) ? chatGroupDto.getName() : chatGroupDto.getRoomId();
        } else {
            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser();
            return chatGroupDto.getUsers()
                    .stream()
                    .filter(user -> !user.getAccountId().equals(authorizedUser.getId()))
                    .map(ChatUserDto::getAccount)
                    .map(this::accountsToChatGroupName)
                    .collect(Collectors.joining(","));
        }
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

    @Override
    public ChatGroupDto getChatGroupDto() {
        return chatGroupDto;
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
            observer.createMessageToUser(chatGroupDto, chatUserDto);
        }
    }

    private void sendMessage(ChatMessageDto chatMessageDto) {
        chatClient.send(chatGroupDto.getRoomId(), chatMessageDto);

        for (ChatRoomObserver observer : observers) {
            observer.sendMessage(chatGroupDto, chatMessageDto);
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
                observer.getMessage(chatGroupDto, message);
            }
        }
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;
        chatClient.subscribeRoom(chatGroupDto.getRoomId(), this);
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        chatClient.unSubscribe(this);
        this.chatClient = null;
    }

    @Override
    public void connectUser(ChatUserDto chatUserDto) {
        chatGroupDto.getUsers().add(chatUserDto);

        if(isViewInit()) {
            Tools.runOnWithOutThreadFX(() -> {
                userListVc.addUser(chatUserDto);

                /*/ChatMessageDto serverMessage = chatMessageContentVc
                        .createServerMessage("Пользователь \"" + chatUserDto.getName() + "\" вошел в чат");
                chatMessageContentVc.addMessage(serverMessage);
                */

                for (ChatRoomObserver observer : observers) {
                    observer.connectUser(chatGroupDto, chatUserDto);
                }
            });
        }
    }

    @Override
    public void disconnectUser(Long accountId) {
        Optional<ChatUserDto> chatUserDtoOptional = chatGroupDto.getUsers()
                .stream()
                .filter(user -> user.getAccountId()
                .equals(accountId))
                .findFirst();

        if(chatUserDtoOptional.isPresent()) {
            ChatUserDto chatUserDto = chatUserDtoOptional.get();

            chatGroupDto.getUsers().remove(chatUserDto);

            if(isViewInit()) {
                Tools.runOnWithOutThreadFX(() -> {
                    userListVc.removeUser(chatUserDto);

                    /*ChatMessageDto serverMessage = chatMessageContentVc
                            .createServerMessage("Пользователь \"" + chatUserDto.getName() + "\" вышел из чата");
                    chatMessageContentVc.addMessage(serverMessage);
                    */
                });
            }

            for (ChatRoomObserver observer : observers) {
                observer.disconnectUser(chatGroupDto, chatUserDto);
            }
        }
    }
}
