package ru.kolaer.client.chat.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.UserListObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class UserListVcImpl implements UserListVc {
    private final ChatGroupDto chatGroupDto;
    private BorderPane mainPane;
    private final List<UserListObserver> userListObservers = new ArrayList<>();
    private ObservableList<ChatUserDto> items = FXCollections.observableArrayList();
    private String subscriptionId;
    private ListView<ChatUserDto> usersListView;

    public UserListVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();

        usersListView = new ListView<>(items);
        usersListView.setCellFactory(param -> new ListCell<ChatUserDto>(){
            @Override
            protected void updateItem(ChatUserDto item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setText("");
                } else {
                    setText(item.getName());
                }
            }
        });
        usersListView.getSelectionModel().selectedItemProperty().addListener(c -> {
            Optional.ofNullable(usersListView.getSelectionModel().getSelectedItem())
                    .ifPresent(selected -> userListObservers.forEach(obs -> obs.selected(selected)));
        });
        items.addListener((ListChangeListener<? super ChatUserDto>) c -> {
            if(c.next()) {
                c.getAddedSubList().forEach(user -> userListObservers.forEach(obs -> obs.connectUser(user)));
                c.getRemoved().forEach(user -> userListObservers.forEach(obs -> obs.disconnectUser(user)));
            }
        });

        mainPane.setCenter(usersListView);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        chatClient.subscribeInfo(this);
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        chatClient.unSubscribe(this);
        Tools.runOnWithOutThreadFX(() -> items.clear());
    }

    @Override
    public void setUsers(List<ChatUserDto> users) {
        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        Tools.runOnWithOutThreadFX(() -> items.setAll(users.stream()
                .filter(chatUserDto -> !chatUserDto.getAccountId().equals(authorizedUser.getId()))
                .collect(Collectors.toList()))
        );
    }

    @Override
    public void registerObserver(UserListObserver observer) {
        userListObservers.add(observer);
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
                Tools.runOnWithOutThreadFX(() -> items.add(activeByIdAccountResponse.getResponse()));
            }
        } else if(info.getCommand() == ChatInfoCommand.DISCONNECT) {
            Tools.runOnWithOutThreadFX(() ->
                    items.removeIf(chatUserDto ->
                            info.getAccountId().equals(chatUserDto.getAccountId()))
            );
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
    public void setSubscriptionId(String id) {
        this.subscriptionId = id;
    }

    @Override
    public String getSubscriptionId() {
        return subscriptionId;
    }
}
