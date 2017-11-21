package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.Date;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatMessageVcImpl implements ChatMessageVc {
    private final ChatGroupDto chatGroupDto;
    private BorderPane mainPane;
    private UserListVc userListVc;
    private ListView<ChatMessageDto> chatMessageDtoListView;
    private ChatClient chatClient;
    private TextArea textArea;

    public ChatMessageVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<ChatMessageVc> viewVisit) {
        mainPane = new BorderPane();

        chatMessageDtoListView = new ListView<>();
        chatMessageDtoListView.setCellFactory(param -> new TextFieldListCell<ChatMessageDto>() {
            @Override
            public void updateItem(ChatMessageDto item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setText("");
                } else {
                    setText(item.getFromAccount().getChatName() + ": " + item.getMessage());
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(chatMessageDtoListView);
        scrollPane.setFitToWidth(true);

        mainPane.setCenter(scrollPane);

        this.textArea = new TextArea();

        Button button = new Button("SEND");
        button.setOnAction(e -> {
            if(chatClient != null) {
                chatClient.send(chatGroupDto.getName(), createMessage(textArea.getText()));
            }
        });

        mainPane.setBottom(new HBox(textArea, button));

        viewVisit.accept(this);
    }

    private ChatMessageDto createMessage(String message) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setMessage(message);
        chatMessageDto.setCreateMessage(new Date());
        chatMessageDto.setRoom(chatGroupDto.getName());
        chatMessageDto.setFromAccount(UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser());
        return chatMessageDto;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public void handleFrame(StompHeaders headers, ChatMessageDto message) {
        if(chatMessageDtoListView != null) {
            Tools.runOnWithOutThreadFX(() -> {
                chatMessageDtoListView.getItems().add(message);
                chatMessageDtoListView.scrollTo(message);
            });
        }
    }

    @Override
    public Node getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;
        chatClient.subscribeRoom(chatGroupDto.getName(), this);
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }
}
