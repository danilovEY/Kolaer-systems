package ru.kolaer.client.chat.runnable;

import javafx.scene.Node;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.observers.AuthenticationObserver;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatClientImpl;
import ru.kolaer.client.chat.view.*;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class ChatPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private MainChatContentVcImpl mainChatContentVc;
    private ChatClient chatClient;
    private UserListVc userListVc;
    private TabChatRoomVc tabChatRoomVc;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        chatClient = new ChatClientImpl(editorKit.getUSNetwork().getKolaerWebServer().getUrl());
        if(editorKit.getAuthentication().isAuthentication()) {
            login(editorKit.getAuthentication().getAuthorizedUser());
        }

        editorKit.getAuthentication().registerObserver(this);

        userListVc = new UserListVcImpl();
        tabChatRoomVc = new TabChatRoomVcImpl();
        mainChatContentVc = new MainChatContentVcImpl();
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void initView(Consumer<UniformSystemPlugin> viewVisit) {
        mainChatContentVc.initView(initMain -> {
            tabChatRoomVc.initView(initMain::setTabChatRoomVc);
            userListVc.initView(initMain::setUserListVc);

            chatClient.registerObserver(userListVc);
            chatClient.registerObserver(tabChatRoomVc);

            if(chatClient.isConnect()){
                userListVc.connect(chatClient);
                tabChatRoomVc.connect(chatClient);
            }

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
    }

    @Override
    public void logout(AccountDto account) {
        chatClient.close();
    }
}