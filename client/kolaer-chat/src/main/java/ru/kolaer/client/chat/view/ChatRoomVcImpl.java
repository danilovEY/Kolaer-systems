package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

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
    public void handleFrame(StompHeaders headers, Object payload) {

    }

    @Override
    public void initView(Consumer<ChatRoomVc> viewVisit) {
        mainPane = new BorderPane();

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
