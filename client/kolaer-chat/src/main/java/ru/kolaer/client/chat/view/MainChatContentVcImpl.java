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
    private SplitPane splitPane;

    @Override
    public void initView(Consumer<MainChatContentVc> viewVisit) {
        mainPane = new BorderPane();

        splitPane = new SplitPane();
        splitPane.getItems().addAll(null, null);

        mainPane.setCenter(splitPane);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void setTabChatRoomVc(TabChatRoomVc tabChatRoomVc) {
        splitPane.getItems().set(0, tabChatRoomVc.getContent());
    }

    @Override
    public void setUserListVc(UserListVc userListVc) {
        splitPane.getItems().set(1, userListVc.getContent());
    }
}
