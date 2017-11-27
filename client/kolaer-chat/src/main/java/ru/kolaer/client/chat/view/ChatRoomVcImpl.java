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
    private ChatMessageContentVc chatMessageContentVc;
    private UserListVc userListVc;
    private ChatClient chatClient;

    public ChatRoomVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
        this.chatMessageContentVc = new ChatMessageContentVcImpl(chatGroupDto);
        this.userListVc = new UserListVcImpl(chatGroupDto);
    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        mainTab = new Tab();
        mainTab.setText(chatGroupDto.getName());
        mainTab.setOnClosed(e -> {
            if(chatClient.isConnect()) {
                disconnect(chatClient);
            }
        });

        SplitPane splitPane = new SplitPane();

        chatMessageContentVc.initView(initMessage -> splitPane.getItems().add(initMessage.getContent()));

        userListVc.initView(initUserList -> splitPane.getItems().add(initUserList.getContent()));
        userListVc.setUsers(chatGroupDto.getUsers());
        userListVc.registerObserver(chatMessageContentVc);

        mainTab.setContent(splitPane);

        viewVisit.accept(this);
    }

    @Override
    public Tab getContent() {
        return mainTab;
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;
        userListVc.connect(chatClient);
        chatMessageContentVc.connect(chatClient);
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        userListVc.disconnect(chatClient);
        chatMessageContentVc.disconnect(chatClient);
        this.chatClient = null;
    }

    public ChatMessageContentVc getChatMessageContentVc() {
        return chatMessageContentVc;
    }

    public UserListVc getUserListVc() {
        return userListVc;
    }
}
