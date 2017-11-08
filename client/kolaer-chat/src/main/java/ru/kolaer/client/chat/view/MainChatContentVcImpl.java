package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
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

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(null, null);

        UserListVc userListVc = new UserListVcImpl();
        TabChatRoomVc tabChatRoomVc = new TabChatRoomVcImpl();

        userListVc.initView(initUserList -> splitPane.getItems().set(1, initUserList.getContent()));
        tabChatRoomVc.initView(initTab -> splitPane.getItems().set(0, initTab.getContent()));

        mainPane.setCenter(splitPane);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }
}
