package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatInfoHandler;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.UserListObserver;

import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface UserListVc extends BaseView<UserListVc, Parent>, ChatObserver, ChatInfoHandler {
    void setUsers(List<ChatUserDto> users);
    void registerObserver(UserListObserver observer);
}
