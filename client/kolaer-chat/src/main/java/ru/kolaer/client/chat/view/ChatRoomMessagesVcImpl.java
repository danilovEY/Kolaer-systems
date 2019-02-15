package ru.kolaer.client.chat.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.common.constant.assess.ClientChatAccessConstant;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.kolchat.*;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatRoomMessagesVcImpl implements ChatRoomMessagesVc {
    private final ChatRoomDto chatRoomDto;

    private final ObservableList<ChatMessageVc> messages = FXCollections.observableArrayList();

    private final SimpleStringProperty titleLabelProperty = new SimpleStringProperty();

    private BorderPane mainPane;
    private ListView<ChatMessageVc> chatMessageDtoListView;

    private TextArea textArea;
    private Consumer<ChatMessageDto> chatMessageConsumer;
    private boolean selected;

    public ChatRoomMessagesVcImpl(ChatRoomDto chatRoomDto) {
        this.chatRoomDto = chatRoomDto;
    }

    @Override
    public void initView(Consumer<ChatRoomMessagesVc> viewVisit) {
        mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label();
        titleLabel.setWrapText(true);
        titleLabel.setFont(Font.font(null, FontWeight.NORMAL, 20));
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.textProperty().bind(titleLabelProperty);

        mainPane.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        chatMessageDtoListView = new ListView<>(messages);
        chatMessageDtoListView.getStyleClass().add("chat-message-list-view");
        chatMessageDtoListView.setCellFactory(param -> new ListCell<ChatMessageVc>() {
            @Override
            public void updateItem(ChatMessageVc item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setGraphic(null);
                } else {
                    if(!item.isViewInit()) {
                        item.initView(initList -> setGraphic(initList.getContent()));
                    } else {
                        setGraphic(item.getContent());
                    }
                }
            }
        });

        if(!messages.isEmpty()) {
            chatMessageDtoListView.scrollTo(messages.size() - 1);
        }

        mainPane.setCenter(chatMessageDtoListView);

        MenuItem hideMessages = new MenuItem("Скрыть сообщение");
        MenuItem deleteMessages = new MenuItem("Удалить сообщение");
        deleteMessages.setOnAction(e -> {
            ObservableList<ChatMessageVc> selectedMessages = chatMessageDtoListView.getSelectionModel().getSelectedItems();
            if(selectedMessages.isEmpty()) {
                return;
            }

            List<Long> removeMessage = selectedMessages.stream()
                    .map(ChatMessageVc::getChatMessageDto)
                    .map(ChatMessageDto::getId)
                    .collect(Collectors.toList());

            ServerResponse serverResponse;

            if(UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser()
                    .hasAccess(ClientChatAccessConstant.CHAT_DELETE_MESSAGE)
            ) {
                serverResponse = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .deleteMessage(new IdsDto(removeMessage));
            } else {
                serverResponse = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .hideMessage(new IdsDto(removeMessage));
            }

            if (serverResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(serverResponse.getExceptionMessage());
            }
        });

        hideMessages.setOnAction(e -> {
            ObservableList<ChatMessageVc> selectedMessages = chatMessageDtoListView.getSelectionModel().getSelectedItems();
            if(selectedMessages.isEmpty()) {
                return;
            }

            List<Long> removeMessage = selectedMessages.stream()
                    .map(ChatMessageVc::getChatMessageDto)
                    .map(ChatMessageDto::getId)
                    .collect(Collectors.toList());

            ServerResponse serverResponse = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .hideMessage(new IdsDto(removeMessage));

            if (serverResponse.isServerError()) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(serverResponse.getExceptionMessage());
            }
        });

        ContextMenu contextMenu = new ContextMenu(deleteMessages);
        contextMenu.setOnShowing(e -> {
            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser();
//            if(authorizedUser.isAccessOit()) {
//                if(!contextMenu.getItems().contains(hideMessages)) {
//                    contextMenu.getItems().add(hideMessages); TODO: refactoring
//                }
//            } else {
//                contextMenu.getItems().remove(hideMessages);
//            }
        });

        chatMessageDtoListView.setContextMenu(contextMenu);

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
        if(!textArea.getText().trim().isEmpty() && chatMessageConsumer != null) {
            String message = textArea.getText();

            if(message.length() > 4096) {
                String[] splitMessage = message.split("(?<=\\G.{4090})");
                for (int i = 0; i < splitMessage.length; i++) {
                    String messagePack = splitMessage[i];

                    if(i < splitMessage.length - 1) {
                        messagePack += "...";
                    }

                    chatMessageConsumer.accept(createMessage(messagePack));

                }
            } else {
                chatMessageConsumer.accept(createMessage(message));
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
        chatMessageDto.setRead(true);
        chatMessageDto.setFromAccount(convertToChatUser(UniformSystemEditorKitSingleton.getInstance().getAuthentication().getAuthorizedUser()));
        return chatMessageDto;
    }

    private ChatUserDto convertToChatUser(AccountDto accountDto) {
        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setStatus(ChatUserStatus.ONLINE);
        chatUserDto.setAccountId(accountDto.getId());
        return chatUserDto;
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void addMessage(ChatMessageDto chatMessageDto) {
        ChatMessageVc chatMessageVc = new ChatMessageVcImpl(chatMessageDto);
        messages.add(chatMessageVc);
        if(isViewInit() && selected) {
            chatMessageDtoListView.scrollTo(chatMessageVc);
        }
    }

    @Override
    public void receivedMessage(ChatRoomVc chatRoomVc, ChatMessageDto chatMessageDto) {
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
    public List<ChatMessageVc> getMessages() {
        return messages;
    }

    @Override
    public void setSendMessage(Consumer<ChatMessageDto> consumer) {
        this.chatMessageConsumer = consumer;
    }

    @Override
    public void setTitle(String title) {
        Tools.runOnWithOutThreadFX(() -> titleLabelProperty.set(title));
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(isViewInit() && !messages.isEmpty()) {
            chatMessageDtoListView.scrollTo(messages.size() - 1);
        }
    }

    @Override
    public void hideMessage(ChatMessageDto chatMessageDto) {
        Tools.runOnWithOutThreadFX(() -> {
            for (ChatMessageVc message : messages) {
                if (message.getChatMessageDto().getId().equals(chatMessageDto.getId())) {
                    message.updateMessage(chatMessageDto);
                }
            }
        });
    }

    @Override
    public void removeMessage(ChatMessageDto chatMessageDto) {
        Tools.runOnWithOutThreadFX(() -> this.messages
                .removeIf(message -> chatMessageDto.getId().equals(message.getChatMessageDto().getId())));

    }

    @Override
    public void removeMessages(List<ChatMessageDto> messages) {
        Tools.runOnWithOutThreadFX(() -> {
            Map<Long, ChatMessageDto> messagesMap = messages
                    .stream()
                    .collect(Collectors.toMap(ChatMessageDto::getId, Function.identity()));

            this.messages.removeIf(message -> messagesMap.containsKey(message.getChatMessageDto().getId()));
        });
    }

    @Override
    public void connect(ChatClient chatClient) {

    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void close(ChatClient chatClient) {
        messages.clear();
    }
}
