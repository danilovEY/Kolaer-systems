package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class TabChatRoomVcImpl implements TabChatRoomVc {
    private BorderPane mainPane;

    @Override
    public void initView(Consumer<TabChatRoomVc> viewVisit) {
        mainPane = new BorderPane();

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }
}
