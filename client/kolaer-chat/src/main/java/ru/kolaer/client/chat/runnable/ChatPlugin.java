package ru.kolaer.client.chat.runnable;

import javafx.scene.Node;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatClientImpl;
import ru.kolaer.client.chat.view.RoomListVc;
import ru.kolaer.client.chat.view.RoomListVcImpl;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private ChatClient chatClient;
    private RoomListVc roomListVc;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        chatClient = new ChatClientImpl(editorKit.getUSNetwork().getKolaerWebServer().getUrl());
        if(editorKit.getAuthentication().isAuthentication()) {
            login(editorKit.getAuthentication().getAuthorizedUser());
        }

        editorKit.getAuthentication().registerObserver(this);
        roomListVc = new RoomListVcImpl();
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        chatClient.close();
    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        roomListVc.initView(initTab -> {
            chatClient.registerObserver(roomListVc);

            if(chatClient.isConnect()){
                roomListVc.connect(chatClient);
            }

            viewVisit.accept(this);
        });
    }

    @Override
    public Node getContent() {
        return roomListVc.getContent();
    }

    @Override
    public void login(AccountDto account) {
        chatClient.start();
    }

    @Override
    public void logout(AccountDto account) {
        chatClient.close();
    }
}