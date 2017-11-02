package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class MainChatContentVcImpl implements MainChatContentVc {
    private BorderPane mainPane;

    @Override
    public void initView(Consumer<MainChatContentVc> viewVisit) {
        mainPane = new BorderPane();

        UserListVc userListVc = new UserListVcImpl();
        TabChatRoomVc tabChatRoomVc = new TabChatRoomVcImpl();

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }
}
