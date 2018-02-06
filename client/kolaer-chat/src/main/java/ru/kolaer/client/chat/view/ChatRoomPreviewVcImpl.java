package ru.kolaer.client.chat.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserStatus;
import ru.kolaer.api.tools.Tools;

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
    private final SimpleStringProperty lastMessageProperty = new SimpleStringProperty();
    private final SimpleStringProperty messageCountProperty = new SimpleStringProperty();

    private boolean selected;
    private int unreadCountMessage = 0;

    private BorderPane mainPane;
    private Label titleLabel;
    private Label statusLabel;
    private Label lastMessageLabel;
    private Label messageCountLabel;

    public ChatRoomPreviewVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if(selected) {
            Tools.runOnWithOutThreadFX(() -> {
                lastMessageProperty.set("");
                messageCountProperty.set("");
                unreadCountMessage = 0;
            });
        }
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    public void setTitle(String titleLabel) {
        this.titleProperty.set(titleLabel);
    }

    public void setStatus(ChatUserStatus statusLabel) {
        this.statusProperty.set(Optional.ofNullable(statusLabel).map(this::convertStatus).orElse(""));
    }

    private String convertStatus(ChatUserStatus chatUserStatus) {
        switch (chatUserStatus) {
            case OFFLINE: return "Не в сети";
            case ONLINE: return "В сети";
            default: return chatUserStatus.name();
        }
    }

    @Override
    public void initView(Consumer<ChatRoomPreviewVc> viewVisit) {
        mainPane = new BorderPane();

        titleLabel = new Label();
        titleLabel.setFont(Font.font(null, FontWeight.NORMAL, 15));
        titleLabel.textProperty().bind(titleProperty);

        lastMessageLabel = new Label();
        lastMessageLabel.setFont(Font.font(null, FontWeight.NORMAL, 10));
        lastMessageLabel.textProperty().bind(lastMessageProperty);

        statusLabel = new Label();
        statusLabel.setFont(Font.font(null, FontWeight.NORMAL, 15));
        statusLabel.textProperty().bind(statusProperty);

        messageCountLabel = new Label();
        messageCountLabel.setFont(Font.font(null, FontWeight.NORMAL, 10));
        messageCountLabel.textProperty().bind(messageCountProperty);

        mainPane.setCenter(titleLabel);
        mainPane.setBottom(lastMessageLabel);
        mainPane.setRight(messageCountLabel);
        mainPane.setLeft(statusLabel);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void receivedMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto) {
        if(!selected) {
            Tools.runOnWithOutThreadFX(() -> {
                lastMessageProperty.set("Новое сообщение " + Tools.dateTimeToString(chatMessageDto.getCreateMessage()));
                messageCountProperty.setValue("[+" + ++unreadCountMessage + "]");
            });
        }
    }
}
