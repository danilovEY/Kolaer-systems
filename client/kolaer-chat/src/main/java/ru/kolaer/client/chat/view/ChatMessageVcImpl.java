package ru.kolaer.client.chat.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
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
    private BorderPane mainPane;

    public ChatMessageVcImpl(ChatMessageDto chatMessageDto) {
        this.chatMessageDto = chatMessageDto;
    }

    @Override
    public void initView(Consumer<ChatMessageVc> viewVisit) {
        mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));
        mainPane.getStyleClass().add("chat-message");

        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        Label copyable = new Label();
        copyable.setWrapText(true);
        copyable.setFont(Font.font(null, FontWeight.BOLD, 20));
        copyable.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    TextArea textarea = new TextArea(copyable.getText());
                    textarea.setPrefHeight(copyable.getHeight() + 10);
                    copyable.setVisible(false);
                    mainPane.setTop(textarea);

                    textarea.requestFocus();
                    textarea.selectAll();

                    textarea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue) {
                            mainPane.setTop(null);
                            copyable.setVisible(true);
                        }
                    });
                    textarea.setOnKeyPressed(event ->{
                        if(event.getCode().toString().equals("ENTER|ESCAPE")) {
                            mainPane.setTop(null);
                            copyable.setVisible(true);
                        }
                    });
                }
            }
        });

        if(chatMessageDto.getType() == ChatMessageType.SERVER_INFO) {
            String message = chatMessageDto.getMessage() +
                    System.lineSeparator() +
                    Tools.dateTimeToString(chatMessageDto.getCreateMessage());

            copyable.setText(message);
            copyable.setAlignment(Pos.CENTER);
            copyable.setTextAlignment(TextAlignment.CENTER);
            mainPane.setCenter(copyable);
            mainPane.getStyleClass().add("chat-message-server");
        } else if(chatMessageDto.getFromAccount() == null || chatMessageDto.getFromAccount().getId().equals(authorizedUser.getId())){
            String message = chatMessageDto.getMessage() +
                    System.lineSeparator() +
                    Tools.dateTimeToString(chatMessageDto.getCreateMessage());

            copyable.setText(message);
            copyable.setAlignment(Pos.CENTER_RIGHT);
            copyable.setTextAlignment(TextAlignment.RIGHT);
            mainPane.setRight(copyable);
            mainPane.getStyleClass().add("chat-message-user");
        } else {
            String username = chatMessageDto.getFromAccount().getChatName();
            if(StringUtils.isEmpty(username)) {
                EmployeeDto employee = chatMessageDto.getFromAccount().getEmployee();
                if(employee != null) {
                    username = employee.getInitials();
                } else {
                    username = chatMessageDto.getFromAccount().getUsername();
                }
            }

            String message = username +
                    System.lineSeparator() +
                    chatMessageDto.getMessage() +
                    System.lineSeparator() +
                    Tools.dateTimeToString(chatMessageDto.getCreateMessage());

            copyable.setText(message);
            copyable.setAlignment(Pos.CENTER_LEFT);
            copyable.setTextAlignment(TextAlignment.LEFT);
            mainPane.setLeft(copyable);
            mainPane.getStyleClass().add("chat-message-other-user");
        }

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
