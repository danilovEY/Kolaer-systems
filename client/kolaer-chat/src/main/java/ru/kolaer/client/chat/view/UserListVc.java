package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface UserListVc extends BaseView<UserListVc, Parent> {
    void setUsers(List<ChatUserDto> users);
    void addUser(ChatUserDto chatUserDto);
    void removeUser(ChatUserDto chatUserDto);

    void setOnCreateMessageToUser(Consumer<List<ChatUserDto>> consumer);
}
