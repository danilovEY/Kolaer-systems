package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.Date;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatMessageContentVcImpl implements ChatMessageContentVc {
    private final ChatGroupDto chatGroupDto;
    private BorderPane mainPane;
    private ListView<ChatMessageDto> chatMessageDtoListView;
    private ChatClient chatClient;
    private TextArea textArea;
    private String subscriptionId;

    public ChatMessageContentVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<ChatMessageContentVc> viewVisit) {
        mainPane = new BorderPane();

        chatMessageDtoListView = new ListView<>();
        chatMessageDtoListView.setCellFactory(param -> new ListCell<ChatMessageDto>() {
            @Override
            public void updateItem(ChatMessageDto item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setGraphic(null);
                } else {
                    ChatMessageVc chatMessageVc = new ChatMessageVcImpl(item);
                    chatMessageVc.initView(initList -> setGraphic(initList.getContent()));
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(chatMessageDtoListView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        mainPane.setCenter(scrollPane);

        textArea = new TextArea();
        textArea.setPromptText("Введите сообщение...");
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                if(keyEvent.isControlDown()) {
                    textArea.appendText(System.getProperty("line.separator"));
                } else {
                    sendMessage();
                    keyEvent.consume();
                }
            }
        });

        Button button = new Button("Отправить");
        button.setPrefHeight(43);
        button.setPrefWidth(150);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setOnAction(e -> sendMessage());

        BorderPane inputPane = new BorderPane();
        inputPane.setCenter(textArea);
        inputPane.setRight(button);
        inputPane.setMaxHeight(50);

        mainPane.setBottom(inputPane);

        viewVisit.accept(this);
    }

    private void sendMessage() {
        if(chatClient != null && !textArea.getText().trim().isEmpty()) {
            chatClient.send(chatGroupDto.getName(), createMessage(textArea.getText()));
            textArea.setText("");
        }
    }

    private ChatMessageDto createMessage(String message) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setType(ChatMessageType.USER);
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
    public void setSubscriptionId(String id) {
        this.subscriptionId = id;
    }

    @Override
    public String getSubscriptionId() {
        return subscriptionId;
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
        chatClient.unSubscribe(this);
        Tools.runOnWithOutThreadFX(() -> chatMessageDtoListView.getItems().clear());
    }

    @Override
    public void connectUser(ChatUserDto chatUserDto) {
        Tools.runOnWithOutThreadFX(() -> chatMessageDtoListView.getItems()
                .add(createServerMessage("Пользователь \"" + chatUserDto.getName() + "\" вошел в чат"))
        );
    }

    @Override
    public void disconnectUser(ChatUserDto chatUserDto) {
        Tools.runOnWithOutThreadFX(() -> chatMessageDtoListView.getItems()
                .add(createServerMessage("Пользователь \"" + chatUserDto.getName() + "\" вышел в чат"))
        );
    }

    @Override
    public void selected(ChatUserDto chatUserDto) {

    }

    private ChatMessageDto createServerMessage(String text) {
        ChatMessageDto serverMessage = new ChatMessageDto();
        serverMessage.setType(ChatMessageType.SERVER);
        serverMessage.setCreateMessage(new Date());
        serverMessage.setMessage(text);

        return serverMessage;
    }
}
