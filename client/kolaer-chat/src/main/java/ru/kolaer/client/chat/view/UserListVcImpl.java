package ru.kolaer.client.chat.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class UserListVcImpl implements UserListVc {
    private final ChatGroupDto chatGroupDto;
    private final ObservableList<ChatUserDto> items = FXCollections.observableArrayList();

    private BorderPane mainPane;
    private ListView<ChatUserDto> usersListView;
    private MenuItem createMessageToUser;

    public UserListVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();

        usersListView = new ListView<>(items);
        usersListView.setPlaceholder(new Label("В этом чате только вы"));
        usersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        usersListView.setCellFactory(param -> new ListCell<ChatUserDto>(){
            @Override
            protected void updateItem(ChatUserDto item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setText("");
                } else {
                    setText(item.getName());
                    if(getFont().getSize() < 15) {
                        setFont(Font.font(null, FontWeight.BOLD, 15));
                    }
                }
            }
        });

        createMessageToUser = new MenuItem("Создать сообщение");

        ContextMenu contextMenu = new ContextMenu(createMessageToUser);
        usersListView.setContextMenu(contextMenu);

        mainPane.setCenter(usersListView);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
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
    public void addUser(ChatUserDto chatUserDto) {
        items.add(chatUserDto);
    }

    @Override
    public void removeUser(ChatUserDto chatUserDto) {
        items.remove(chatUserDto);
    }

    @Override
    public void setOnCreateMessageToUser(Consumer<List<ChatUserDto>> consumer) {
        createMessageToUser.setOnAction(e -> {
            ObservableList<ChatUserDto> selectedItems = usersListView.getSelectionModel().getSelectedItems();
            if(!selectedItems.isEmpty()) {
                consumer.accept(selectedItems);
            }
        });
    }

}
