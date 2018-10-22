package ru.kolaer.client.chat.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatUserStatus;
import ru.kolaer.common.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 05.02.2018.
 */
@Slf4j
public class ChatRoomPreviewVcImpl implements ChatRoomPreviewVc {

    private final SimpleStringProperty titleProperty = new SimpleStringProperty();
    private final SimpleStringProperty lastMessageProperty = new SimpleStringProperty();
    private final SimpleStringProperty messageCountProperty = new SimpleStringProperty();

    private boolean selected;
    private int unreadCountMessage = 0;

    private BorderPane mainPane;
    private ChatRoomDto chatRoomDto;
    private Label lastMessageLabel;
    private Label messageCountLabel;

    private ChatUserStatus chatUserStatus;
    private Canvas statusIcon;
    private Label titleLabel;

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

                if(isViewInit()) {
                    mainPane.setBottom(null);
                    mainPane.setRight(null);
                }
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

    public void setStatus(ChatUserStatus chatUserStatus) {
        Tools.runOnWithOutThreadFX(() -> {
            this.chatUserStatus = chatUserStatus;
            if (isViewInit()) {
                GraphicsContext graphicsContext2D = statusIcon.getGraphicsContext2D();
                if(chatUserStatus == ChatUserStatus.ONLINE) {
                    graphicsContext2D.setFill(Color.LIGHTGREEN);
                } else {
                    graphicsContext2D.setFill(Color.GRAY);
                }

                graphicsContext2D.fillOval(0, 0, 15, 15);
                graphicsContext2D.fill();
                graphicsContext2D.save();
            }
        });
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

        statusIcon = new Canvas(15, 15);
        GraphicsContext graphicsContext2D = statusIcon.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.GRAY);
        graphicsContext2D.fillOval(0, 0, 15, 15);
        graphicsContext2D.save();

        messageCountLabel = new Label();
        messageCountLabel.setFont(Font.font(null, FontWeight.NORMAL, 10));
        messageCountLabel.textProperty().bind(messageCountProperty);

        mainPane.setCenter(titleLabel);
        mainPane.setLeft(statusIcon);

        if(unreadCountMessage > 0) {
            mainPane.setBottom(lastMessageLabel);
            mainPane.setRight(messageCountLabel);
        }

        Optional.ofNullable(chatUserStatus)
                .ifPresent(this::setStatus);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void receivedMessage(ChatRoomVc chatRoomVc, ChatMessageDto chatMessageDto) {
        if(!selected) {
            Tools.runOnWithOutThreadFX(() -> {
                lastMessageProperty.set("Сообщение от " + Tools.dateTimeToString(chatMessageDto.getCreateMessage()));
                messageCountProperty.setValue("[+" + ++unreadCountMessage + "]");

                if(isViewInit()) {
                    mainPane.setBottom(lastMessageLabel);
                    mainPane.setRight(messageCountLabel);
                }
            });
        }
    }

    @Override
    public void connect(ChatClient chatClient) {

    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void close(ChatClient chatClient) {

    }
}
