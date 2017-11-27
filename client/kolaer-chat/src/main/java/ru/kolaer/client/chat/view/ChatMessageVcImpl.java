package ru.kolaer.client.chat.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
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

        if(chatMessageDto.getType() == ChatMessageType.SERVER) {
            mainPane.setCenter(new Label(chatMessageDto.getMessage()));
        } else if(chatMessageDto.getFromAccount() == null || chatMessageDto.getFromAccount().getId().equals(authorizedUser.getId())){
            mainPane.setRight(new Label(chatMessageDto.getMessage()));
        } else {
            mainPane.setLeft(new Label(chatMessageDto.getFromAccount().getChatName() + ": " + chatMessageDto.getMessage()));
        }

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
