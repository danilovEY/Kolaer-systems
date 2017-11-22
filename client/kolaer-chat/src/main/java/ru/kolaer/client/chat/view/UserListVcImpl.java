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
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.UserListObserver;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class UserListVcImpl implements UserListVc {
    private final ChatGroupDto chatGroupDto;
    private BorderPane mainPane;
    private ObservableList<ChatUserDto> items = FXCollections.observableArrayList();

    public UserListVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();

        ListView<ChatUserDto> users = new ListView<>(items);

        users.setCellFactory(param -> new ListCell<ChatUserDto>(){
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

        mainPane.setCenter(users);

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

    }

    @Override
    public void setUsers(List<ChatUserDto> users) {
        items.setAll(users);
    }

    @Override
    public void registerObserver(UserListObserver observer) {
        items.addListener((ListChangeListener<? super ChatUserDto>) c -> {
            if(c.next()) {
                c.getAddedSubList().forEach(observer::connectUser);
                c.getRemoved().forEach(observer::disconnectUser);
            }
        });
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
}
