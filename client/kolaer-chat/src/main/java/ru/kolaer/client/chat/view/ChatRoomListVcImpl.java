package ru.kolaer.client.chat.view;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.tools.Tools;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.02.2018.
 */
@Slf4j
public class ChatRoomListVcImpl implements ChatRoomListVc {
    private final ObservableList<ChatRoomVc> chatRooms;
    private BorderPane mainPane;
    private ListView<ChatRoomVc> roomListView;
    private ChatClient chatClient;

    public ChatRoomListVcImpl(ObservableList<ChatRoomVc> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public void initView(Consumer<ChatRoomListVc> viewVisit) {
        mainPane = new BorderPane();

        sorted();

        FilteredList<ChatRoomVc> filteredData = new FilteredList<>(chatRooms, s -> true);

        TextField filterInput = new TextField();
        filterInput.textProperty().addListener(obs->{
            String filter = filterInput.getText();
            if(filter == null || filter.isEmpty()) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.getChatRoomDto().getName().toLowerCase().contains(filter.toLowerCase()));
            }
        });

        roomListView = new ListView<>(filteredData);
        roomListView.setPlaceholder(new Label("Список пуст"));
        roomListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roomListView.setCellFactory(param -> new ListCell<ChatRoomVc>(){
            @Override
            protected void updateItem(ChatRoomVc item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setGraphic(null);
                } else {
                    ChatRoomPreviewVc chatRoomPreviewVc = item.getChatRoomPreviewVc();
                    if(!chatRoomPreviewVc.isViewInit()) {
                        chatRoomPreviewVc.initView(BaseView::empty);
                    }

                    setGraphic(chatRoomPreviewVc.getContent());
                }
            }
        });

        MenuItem createRoom = new MenuItem("Создать приватную комнату");
        MenuItem quitRoom = new MenuItem("Выйти из комнат");
        createRoom.setOnAction(e -> {
            List<Long> userIds = roomListView.getSelectionModel().getSelectedItems()
                    .stream()
                    .filter(chat -> chat.getChatRoomDto().getType() == ChatGroupType.SINGLE)
                    .map(ChatRoomVc::getChatRoomDto)
                    .map(room -> room.getUsers().get(0).getAccountId())
                    .collect(Collectors.toList());

            if(userIds.size() > 1) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .createPrivateRoom(new IdsDto(userIds), null);
            }
        });
        quitRoom.setOnAction(e -> {
            List<ChatRoomDto> rooms = roomListView.getSelectionModel().getSelectedItems()
                    .stream()
                    .filter(chat -> chat.getChatRoomDto().getType() == ChatGroupType.PRIVATE ||
                            chat.getChatRoomDto().getType() == ChatGroupType.PUBLIC)
                    .map(ChatRoomVc::getChatRoomDto)
                    .collect(Collectors.toList());

            if(rooms.isEmpty()) {
                return;
            }

            String roomNames = rooms
                    .stream()
                    .map(ChatRoomDto::getName)
                    .collect(Collectors.joining("," + System.lineSeparator()));

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Вопрос");
            alert.setHeaderText("Выход из групп");
            alert.setContentText("Вы действительно хотите выйти из групп:" + System.lineSeparator() + roomNames);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                List<Long> roomIds = rooms
                        .stream()
                        .map(ChatRoomDto::getId)
                        .collect(Collectors.toList());

                UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .quitFromRoom(new IdsDto(roomIds));
            }
        });

        roomListView.setContextMenu(new ContextMenu(createRoom, quitRoom));

        Label search = new Label("Поиск");
        search.setTextAlignment(TextAlignment.CENTER);
        search.setAlignment(Pos.CENTER);
        search.setPadding(new Insets(10));

        BorderPane searchPane = new BorderPane(filterInput);
        searchPane.setLeft(search);
        searchPane.setPadding(new Insets(10));

        mainPane.setTop(searchPane);
        mainPane.setCenter(roomListView);

        viewVisit.accept(this);
    }

    private int compareRoom(ChatRoomVc chatRoomVcFirst, ChatRoomVc chatRoomVcSecond) {
        return getLastMessageDate(chatRoomVcFirst).toInstant().truncatedTo(ChronoUnit.DAYS)
                .compareTo(getLastMessageDate(chatRoomVcSecond).toInstant()) * -1;
    }

    private Date getLastMessageDate(ChatRoomVc chatRoomVc) {
        List<ChatMessageVc> messages = chatRoomVc.getChatRoomMessagesVc().getMessages();
        if (messages.isEmpty()) {
            return new Date(0);
        } else {
            return messages.get(messages.size() - 1).getChatMessageDto().getCreateMessage();
        }
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void addChatRoomVc(List<ChatRoomVc> chatRoomVcList) {
        chatRooms.addAll(chatRoomVcList);
    }

    @Override
    public void addChatRoomVc(ChatRoomVc chatRoomVc) {
        chatRooms.add(chatRoomVc);
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void disconnect(ChatClient chatClient) {
    }

    @Override
    public void close(ChatClient chatClient) {

    }

    @Override
    public void setOnSelectRoom(Consumer<ChatRoomVc> consumer) {
        roomListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        Optional.ofNullable(newValue).ifPresent(consumer));
    }

    @Override
    public void setSelectRoom(ChatRoomVc chatRoomVc) {
        roomListView.getSelectionModel().select(chatRoomVc);
    }

    @Override
    public void sorted() {
        Tools.runOnWithOutThreadFX(() -> this.chatRooms.sort(this::compareRoom));
    }

    @Override
    public void receivedMessage(ChatRoomVc chatRoomVc, ChatMessageDto chatMessageDto) {
        sorted();
    }
}
