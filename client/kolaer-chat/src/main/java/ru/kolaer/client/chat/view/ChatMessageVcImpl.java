package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

/**
 * Created by danilovey on 22.11.2017.
 */
public class ChatMessageVcImpl implements ChatMessageVc {
    private BorderPane mainPane;



    @Override
    public void initView(Consumer<ChatMessageVc> viewVisit) {
        mainPane = new BorderPane();
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
