package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.chat.ChatGroupDto;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public class UserListVcImpl implements UserListVc {
    private BorderPane mainPane;
    private TreeView<ChatGroupDto> activeUserTreeView;

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();
        activeUserTreeView = new TreeView<>();
        mainPane.setCenter(activeUserTreeView);

        ChatGroupDto rootGroup = new ChatGroupDto();
        rootGroup.setName("Main_test");

        TreeItem<ChatGroupDto> rootItem = new TreeItem<>();
        rootItem.setValue(rootGroup);

        activeUserTreeView.setRoot(rootItem);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }
}
