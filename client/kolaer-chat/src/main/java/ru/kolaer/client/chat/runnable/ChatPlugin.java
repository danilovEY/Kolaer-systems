package ru.kolaer.client.chat.runnable;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.observers.AuthenticationObserver;
import ru.kolaer.common.plugins.UniformSystemPlugin;
import ru.kolaer.common.system.UniformSystemEditorKit;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.ChatClientImpl;
import ru.kolaer.client.chat.view.ChatContentVc;
import ru.kolaer.client.chat.view.ChatContentVcImpl;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatPlugin implements UniformSystemPlugin, AuthenticationObserver {
    private BorderPane mainPane;
    private ChatClient chatClient;
    private ChatContentVc chatContentVc;
    private Label infoLabel;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        chatClient = new ChatClientImpl(editorKit.getUSNetwork().getKolaerWebServer().getUrl());
        chatContentVc = new ChatContentVcImpl(this);

        editorKit.getAuthentication().registerObserver(this);

        if(editorKit.getAuthentication().isAuthentication()) {
            login(editorKit.getAuthentication().getAuthorizedUser());
        }
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
        infoLabel = new Label("Вы не авторизовались!");
        mainPane = new BorderPane(infoLabel);

        chatContentVc.initView(initTab -> {
            chatClient.registerObserver(chatContentVc);

            if(UniformSystemEditorKitSingleton.getInstance().getAuthentication().isAuthentication()) {
                mainPane.setCenter(chatContentVc.getContent());
            }

            viewVisit.accept(this);
        });
    }

    @Override
    public Node getContent() {
        return mainPane;
    }

    @Override
    public void login(AccountDto account) {
        Tools.runOnWithOutThreadFX(() -> {
            if(chatContentVc.isViewInit()) {
                mainPane.setCenter(chatContentVc.getContent());
            }
        });

        chatClient.start();
    }

    @Override
    public void logout(AccountDto account) {
        Tools.runOnWithOutThreadFX(() -> {
            if(chatContentVc.isViewInit()) {
                mainPane.setCenter(infoLabel);
            }
        });

        chatClient.close();
    }
}