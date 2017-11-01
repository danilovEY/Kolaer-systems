package ru.kolaer.client.chat;

/*import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.glassfish.tyrus.container.grizzly.client.GrizzlyContainerProvider;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.api.plugins.UniformSystemPlugin;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;*/

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import ru.kolaer.api.plugins.UniformSystemPlugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by danilovey on 01.11.2017.
 */
public class WebSocketTest implements UniformSystemPlugin {
    private Button button;

    public static void main(String[] args) {
        //Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        //root.setLevel(Level.INFO);
        /*new GrizzlyContainerProvider();
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
                session.subscribe("/topic/chats.test", this);
                ChatMessageDto test = new ChatMessageDto();
                test.setFrom("TEST");
                test.setMessage("test");
                session.send("/app/chats/test", test);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                System.out.println("ZZZ");
                System.out.println(new String(payload));
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                exception.printStackTrace();
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("CCCC");
                //ChatMessageDto msg = (ChatMessageDto) payload;
                System.out.println(payload);
            }
        };
        stompClient.connect("ws://localhost:8080/rest/chat", sessionHandler);

        new Scanner(System.in).nextLine();*/


        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/rest/chat")) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("1");
                    Executors.newSingleThreadExecutor().submit(() -> {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        send("33333");
                    });
                    Executors.newSingleThreadExecutor().submit(() -> send(new Scanner(System.in).nextLine()));
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("2");
                    System.out.println(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("3");
                    System.out.println(reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("4");
                }
            };

            webSocketClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
