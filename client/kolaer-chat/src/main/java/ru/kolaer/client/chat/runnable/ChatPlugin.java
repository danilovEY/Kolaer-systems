package ru.kolaer.client.chat.runnable;

import javafx.scene.Node;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatClientImpl;
import ru.kolaer.client.chat.service.ChatHandler;
import ru.kolaer.client.chat.view.MainChatContentVcImpl;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class ChatPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private MainChatContentVcImpl mainChatContentVc;
    private ChatClient chatClient;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        chatClient = new ChatClientImpl(editorKit.getUSNetwork().getKolaerWebServer().getUrl());
        if(editorKit.getAuthentication().isAuthentication()) {
            login(editorKit.getAuthentication().getAuthorizedUser());
        }

        editorKit.getAuthentication().registerObserver(this);

        mainChatContentVc = new MainChatContentVcImpl();
    }

    @Override
    public void start() throws Exception {
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


    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        mainChatContentVc.initView(initMain -> {

            viewVisit.accept(this);
        });
    }

    @Override
    public Node getContent() {
        return mainChatContentVc.getContent();
    }

    @Override
    public void login(AccountDto account) {
        chatClient.start();

        ChatMessageDto test = new ChatMessageDto();
        test.setFromAccount(account);
        test.setMessage("test");
        chatClient.send("test", test);
    }

    @Override
    public void logout(AccountDto account) {
        chatClient.close();
    }
}