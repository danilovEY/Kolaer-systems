package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 05.02.2018.
 */
@Slf4j
public class ChatRoomVcImpl implements ChatRoomVc {
    private final ChatRoomDto chatRoomDto;
    private boolean selected;
    private String subId;
    private BorderPane mainPane;
    private Label title;
    private Label status;

    public ChatRoomVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
    }

    @Override
    public ChatRoomDto getChatRoomDto() {
        return this.chatRoomDto;
    }

    @Override
    public void selected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        this.mainPane = new BorderPane();
        this.title = new Label();
        this.status = new Label();

        if(chatRoomDto.getType() == ChatGroupType.SINGLE) {
            ChatUserDto chatUserDto = chatRoomDto.getUsers().get(0);

            if(chatUserDto != null) {
                String title = chatUserDto.getName();

                this.title.setText(title);
                this.status.setText(chatUserDto.getStatus().name());
            } else {
                this.title.setText("Unknown");
                this.status.setText("");
            }
        } else {
            String title = chatRoomDto.getName();

            if(StringUtils.isEmpty(title)) {
                title = chatRoomDto.getUsers()
                        .stream()
                        .map(ChatUserDto::getName)
                        .collect(Collectors.joining(","));
            }

            this.title.setText(title);
            this.status.setText("");
        }

        this.mainPane.setCenter(title);
        this.mainPane.setRight(status);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        chatClient.subscribeRoom(this.chatRoomDto, this);
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void handleFrame(StompHeaders headers, ChatMessageDto message) {
        log.info("New message: {}", message);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        handleTransportError(session, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void setSubscriptionId(String id) {
        this.subId = id;
    }

    @Override
    public String getSubscriptionId() {
        return this.subId;
    }
}
