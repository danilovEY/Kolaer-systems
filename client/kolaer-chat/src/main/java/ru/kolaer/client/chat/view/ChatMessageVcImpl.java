package ru.kolaer.client.chat.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;

import java.util.function.Consumer;

/**
 * Created by danilovey on 22.11.2017.
 */
public class ChatMessageVcImpl implements ChatMessageVc {
    private final ChatMessageDto chatMessageDto;
    private StackPane mainPane;

    public ChatMessageVcImpl(ChatMessageDto chatMessageDto) {
        this.chatMessageDto = chatMessageDto;
    }

    @Override
    public void initView(Consumer<ChatMessageVc> viewVisit) {
        mainPane = new StackPane();
        mainPane.setPadding(new Insets(10));
        mainPane.setMinWidth(0);
        mainPane.setPrefWidth(1);
        mainPane.getStyleClass().add("chat-message");

        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        Label copyable = new Label();
        copyable.setWrapText(true);
        copyable.setFont(Font.font(null, FontWeight.NORMAL, 15));
        copyable.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    TextArea textarea = new TextArea(copyable.getText());
                    textarea.setPrefHeight(copyable.getHeight() + 10);
                    copyable.setVisible(false);
                    mainPane.getChildren().add(textarea);

                    textarea.requestFocus();
                    textarea.selectAll();

                    textarea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue) {
                            mainPane.getChildren().add(null);
                            copyable.setVisible(true);
                        }
                    });
                    textarea.setOnKeyPressed(event ->{
                        if(event.getCode().toString().equals("ENTER|ESCAPE")) {
                            mainPane.getChildren().add(null);
                            copyable.setVisible(true);
                        }
                    });
                }
            }
        });

        String message = chatMessageDto.getMessage() +
                System.lineSeparator() +
                Tools.dateTimeToString(chatMessageDto.getCreateMessage());

        if(chatMessageDto.isHide()) {
            message += System.lineSeparator() + "(Сообщение скрыто)";
        }

        if(chatMessageDto.getType() == ChatMessageType.SERVER_INFO) {
            copyable.setText(message);
            copyable.setAlignment(Pos.CENTER);
            copyable.setTextAlignment(TextAlignment.CENTER);
            mainPane.setAlignment(Pos.CENTER);
            mainPane.getChildren().add(copyable);
            mainPane.getStyleClass().add("chat-message-server");
        } else if(chatMessageDto.getFromAccount() == null || chatMessageDto.getFromAccount().getAccountId().equals(authorizedUser.getId())){
            copyable.setText(message);
            copyable.setAlignment(Pos.CENTER_RIGHT);
            copyable.setTextAlignment(TextAlignment.RIGHT);
            mainPane.setAlignment(Pos.CENTER_RIGHT);
            mainPane.getChildren().add(copyable);
            mainPane.getStyleClass().add("chat-message-user");
        } else {
            String username = chatMessageDto.getFromAccount().getName();

            message = username +
                    System.lineSeparator() +
                    message;

            copyable.setText(message);
            copyable.setAlignment(Pos.CENTER_LEFT);
            copyable.setTextAlignment(TextAlignment.LEFT);
            mainPane.setAlignment(Pos.CENTER_LEFT);
            mainPane.getChildren().add(copyable);
            mainPane.getStyleClass().add("chat-message-other-user");
        }

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
