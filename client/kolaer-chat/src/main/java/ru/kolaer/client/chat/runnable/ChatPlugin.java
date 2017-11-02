package ru.kolaer.client.chat.runnable;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatClientImpl;
import ru.kolaer.client.chat.service.ChatHandler;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class ChatPlugin implements UniformSystemPlugin {
    private Button button;

    public static void main(String[] args) {

        ChatClient chatClient = new ChatClientImpl();
        chatClient.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("subs...");
        chatClient.subscribeRoom("test", new ChatHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                System.out.println("ZZZ");
                System.out.println(new String(payload));
                exception.printStackTrace();
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                exception.printStackTrace();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("CCCC");
                ChatMessageDto msg = (ChatMessageDto) payload;
                System.out.println(msg);
            }
        });

        ChatMessageDto test = new ChatMessageDto();
        test.setFrom("TEST");
        test.setMessage("test");
        chatClient.send("test", test);

        new Scanner(System.in).nextLine();
    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        button = new Button("TEST");
        button.setOnAction(e -> main(null));
        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return new BorderPane(button);
    }
}