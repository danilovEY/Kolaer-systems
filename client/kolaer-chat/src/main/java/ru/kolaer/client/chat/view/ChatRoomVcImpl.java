package ru.kolaer.client.chat.view;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatInfoHandler;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class ChatRoomVcImpl implements ChatRoomVc {
    private final ChatGroupDto chatGroupDto;
    private Tab mainTab;
    private ChatMessageContentVc chatMessageContentVc;
    private UserListVc userListVc;

    public ChatRoomVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
        this.chatMessageContentVc = new ChatMessageContentVcImpl(chatGroupDto);
        this.userListVc = new UserListVcImpl(chatGroupDto);
    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        mainTab = new Tab();
        mainTab.setText(chatGroupDto.getName());

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
        chatClient.subscribeInfo(new ChatInfoHandler() {
            @Override
            public void handleFrame(StompHeaders headers, ChatInfoDto info) {
                System.out.println(info);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {

            }
        });
        userListVc.connect(chatClient);
        chatMessageContentVc.connect(chatClient);
    }

    @Override
    public void disconnect(ChatClient chatClient) {
        userListVc.disconnect(chatClient);
        chatMessageContentVc.disconnect(chatClient);
    }

    public ChatMessageContentVc getChatMessageContentVc() {
        return chatMessageContentVc;
    }

    public UserListVc getUserListVc() {
        return userListVc;
    }
}
