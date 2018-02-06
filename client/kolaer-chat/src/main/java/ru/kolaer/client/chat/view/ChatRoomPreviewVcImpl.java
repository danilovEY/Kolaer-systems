package ru.kolaer.client.chat.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserStatus;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 05.02.2018.
 */
@Slf4j
public class ChatRoomPreviewVcImpl implements ChatRoomPreviewVc {
    private final ChatRoomDto chatRoomDto;

    private final SimpleStringProperty titleProperty = new SimpleStringProperty();
    private final SimpleStringProperty statusProperty = new SimpleStringProperty();

    private boolean selected;

    private BorderPane mainPane;
    private Label title;
    private Label status;

    public ChatRoomPreviewVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setTitle(String title) {
        this.titleProperty.set(title);
    }

    @Override
    public void setStatus(ChatUserStatus status) {
        this.statusProperty.set(Optional.ofNullable(status).map(Enum::name).orElse(null));
    }

    @Override
    public void initView(Consumer<ChatRoomPreviewVc> viewVisit) {
        mainPane = new BorderPane();
        title = new Label();
        status = new Label();

        title.textProperty().bind(titleProperty);
        status.textProperty().bind(statusProperty);

        mainPane.setCenter(title);
        mainPane.setRight(status);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }
}
