package ru.kolaer.client.chat.view;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.List;
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

        roomListView = new ListView<>(chatRooms);
        roomListView.setPlaceholder(new Label("Wait"));
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

        MenuItem createRoom = new MenuItem("Создать комнату");
        createRoom.setOnAction(e -> {
            List<Long> userIds = roomListView.getSelectionModel().getSelectedItems()
                    .stream()
                    .filter(chat -> chat.getChatRoomDto().getType() == ChatGroupType.SINGLE)
                    .map(ChatRoomVc::getChatRoomDto)
                    .map(room -> room.getUsers().get(0).getAccountId())
                    .collect(Collectors.toList());

            UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getChatTable()
                    .createPrivateRoom(new IdsDto(userIds), null);
        });

        roomListView.setContextMenu(new ContextMenu(createRoom));

        mainPane.setCenter(roomListView);

        viewVisit.accept(this);
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
                .addListener((observable, oldValue, newValue) -> consumer.accept(newValue));
    }

    @Override
    public void setSelectRoom(ChatRoomVc chatRoomVc) {
        roomListView.getSelectionModel().select(chatRoomVc);
    }
}
