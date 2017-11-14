package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class ChatRoomVcImpl implements ChatRoomVc {
    private BorderPane mainPane;

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public void handleFrame(StompHeaders headers, ChatMessageDto message) {

    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        mainPane = new BorderPane();

        VBox userList = new VBox();
        VBox chatContent = new VBox();

        ScrollPane scrollPane = new ScrollPane();


        mainPane.setLeft(userList);
        mainPane.setCenter(chatContent);

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
