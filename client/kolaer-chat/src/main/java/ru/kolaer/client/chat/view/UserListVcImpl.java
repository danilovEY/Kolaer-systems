package ru.kolaer.client.chat.view;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class UserListVcImpl implements UserListVc {
    private Map<Long, ListView<ChatUserDto>> usersMap = new HashMap<>();
    private final ChatGroupDto chatGroupDto;
    private BorderPane mainPane;
    private ObservableList<ChatUserDto> items;

    public UserListVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();

        ListView<ChatUserDto> users = new ListView<>();

        this.items = users.getItems();

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

    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void setUsers(List<ChatUserDto> users) {
        items.setAll(users);
    }
}
