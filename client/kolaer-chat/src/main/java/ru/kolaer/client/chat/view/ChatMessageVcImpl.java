package ru.kolaer.client.chat.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;

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

        AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser();

        Label copyable = new Label();
        copyable.setWrapText(true);
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

        if(chatMessageDto.getType() == ChatMessageType.SERVER) {
            copyable.setText(chatMessageDto.getMessage());
            mainPane.setCenter(copyable);
        } else if(chatMessageDto.getFromAccount() == null || chatMessageDto.getFromAccount().getId().equals(authorizedUser.getId())){
            copyable.setText(chatMessageDto.getMessage());
            mainPane.setRight(copyable);
        } else {
            copyable.setText(chatMessageDto.getFromAccount().getChatName() + ": " + chatMessageDto.getMessage());
            mainPane.setLeft(copyable);
        }

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
