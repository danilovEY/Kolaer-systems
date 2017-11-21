package ru.kolaer.client.chat.view;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class ChatRoomVcImpl implements ChatRoomVc {
    private final ChatGroupDto chatGroupDto;
    private Tab mainTab;
    private ChatMessageVc chatMessageVc;
    private UserListVc userListVc;

    public ChatRoomVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        mainTab = new Tab();
        mainTab.setText(chatGroupDto.getName());

        SplitPane splitPane = new SplitPane();

        chatMessageVc = new ChatMessageVcImpl(chatGroupDto);
        chatMessageVc.initView(initMessage -> splitPane.getItems().add(initMessage.getContent()));

        userListVc = new UserListVcImpl(chatGroupDto);
        userListVc.initView(initUserList -> splitPane.getItems().add(initUserList.getContent()));
        userListVc.setUsers(chatGroupDto.getUsers());

        mainTab.setContent(splitPane);

        viewVisit.accept(this);
    }

    @Override
    public Tab getContent() {
        return mainTab;
    }

    @Override
    public void connect(ChatClient chatClient) {
        userListVc.connect(chatClient);
        chatMessageVc.connect(chatClient);
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public ChatMessageVc getChatMessageVc() {
        return chatMessageVc;
    }

    @Override
    public UserListVc getUserListVc() {
        return userListVc;
    }
}
