package ru.kolaer.client.chat.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatRoomMessagesVcImpl implements ChatRoomMessagesVc {
    private final ChatRoomDto chatRoomDto;

    private final ObservableList<ChatMessageDto> messages = FXCollections.observableArrayList();

    private BorderPane mainPane;
    private ListView<ChatMessageDto> chatMessageDtoListView;
    private TextArea textArea;

    private Consumer<ChatMessageDto> chatMessageConsumer;

    public ChatRoomMessagesVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
    }

    @Override
    public void initView(Consumer<ChatRoomMessagesVc> viewVisit) {
        mainPane = new BorderPane();

        ServerResponse<Page<ChatMessageDto>> messageByRoomId = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .getMessageByRoomId(chatRoomDto.getId());

        if(messageByRoomId.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(messageByRoomId.getExceptionMessage());
        } else {
            messages.addAll(messageByRoomId.getResponse().getData());
            messages.sort(Comparator.comparing(ChatMessageDto::getId));
        }

        chatMessageDtoListView = new ListView<>(messages);
        chatMessageDtoListView.getStyleClass().add("chat-message-list-view");

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

        MenuItem hideMessages = new MenuItem("Удалить сообщение");
        hideMessages.setOnAction(e -> {
            ObservableList<ChatMessageDto> selectedMessages = chatMessageDtoListView.getSelectionModel().getSelectedItems();
            if(selectedMessages.isEmpty()) {
                return;
            }

            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser();

            List<Long> removeMessage = selectedMessages.stream()
                    .filter(message -> authorizedUser.isAccessOit() || authorizedUser.getId().equals(Optional
                            .ofNullable(message.getFromAccount())
                            .map(AccountDto::getId)
                            .orElse(-1L)))
                    .map(ChatMessageDto::getId)
                    .collect(Collectors.toList());

            ServerResponse serverResponse = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .hideMessage(new IdsDto(removeMessage));
            if(serverResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(serverResponse.getExceptionMessage());
            }
        });

        chatMessageDtoListView.setContextMenu(new ContextMenu(hideMessages));

        textArea = new TextArea();
        textArea.setPromptText("Введите сообщение...");
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (keyEvent.isControlDown()) {
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
            if(chatMessageConsumer != null) {
                chatMessageConsumer.accept(createMessage(textArea.getText()));
            }

            textArea.setText("");
        }
    }

    @Override
    public ChatMessageDto createMessage(String message) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setType(ChatMessageType.USER);
        chatMessageDto.setMessage(message);
        chatMessageDto.setCreateMessage(new Date());
        chatMessageDto.setRoomId(chatRoomDto.getId());
        chatMessageDto.setFromAccount(UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser());
        return chatMessageDto;
    }

    @Override
    public Parent getContent() {
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
    public void receivedMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto) {
        Tools.runOnWithOutThreadFX(() -> addMessage(chatMessageDto));
    }

    @Override
    public ChatMessageDto createServerMessage(String text) {
        ChatMessageDto serverMessage = new ChatMessageDto();
        serverMessage.setType(ChatMessageType.SERVER_INFO);
        serverMessage.setCreateMessage(new Date());
        serverMessage.setMessage(text);

        return serverMessage;
    }

    @Override
    public List<ChatMessageDto> getMessages() {
        return messages;
    }

    @Override
    public void setSendMessage(Consumer<ChatMessageDto> consumer) {
        this.chatMessageConsumer = consumer;
    }


    @Override
    public void setTitle(String title) {

    }

    @Override
    public void removeMessages(List<ChatMessageDto> messages) {
        this.messages.removeAll(messages);
    }
}
