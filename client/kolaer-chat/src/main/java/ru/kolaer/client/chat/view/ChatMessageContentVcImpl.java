package ru.kolaer.client.chat.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;

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
    private TextArea textArea;

    private Consumer<ChatMessageDto> sendMessageConsumer;
    private ObservableList<ChatMessageDto> messages = FXCollections.observableArrayList();

    public ChatMessageContentVcImpl(ChatGroupDto chatGroupDto) {
        this.chatGroupDto = chatGroupDto;
    }

    @Override
    public void initView(Consumer<ChatMessageContentVc> viewVisit) {
        mainPane = new BorderPane();

        chatMessageDtoListView = new ListView<>(messages);
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
        if(!textArea.getText().trim().isEmpty()) {
            if(sendMessageConsumer != null) {
                sendMessageConsumer.accept(createMessage(textArea.getText()));
            }
            textArea.setText("");
        }
    }

    @Override
    public void setOnSendMessage(Consumer<ChatMessageDto> consumer) {
        this.sendMessageConsumer = consumer;
    }

    @Override
    public ChatMessageDto createMessage(String message) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setType(ChatMessageType.USER);
        chatMessageDto.setMessage(message);
        chatMessageDto.setCreateMessage(new Date());
        chatMessageDto.setRoom(chatGroupDto.getRoomId());
        chatMessageDto.setFromAccount(UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser());
        return chatMessageDto;
    }

    @Override
    public Node getContent() {
        return mainPane;
    }

    @Override
    public void addMessage(ChatMessageDto chatMessageDto) {
        messages.add(chatMessageDto);
        if(isViewInit()) {
            chatMessageDtoListView.scrollTo(chatMessageDto);
        }
    }

    @Override
    public ChatMessageDto createServerMessage(String text) {
        ChatMessageDto serverMessage = new ChatMessageDto();
        serverMessage.setType(ChatMessageType.SERVER);
        serverMessage.setCreateMessage(new Date());
        serverMessage.setMessage(text);

        return serverMessage;
    }
}
