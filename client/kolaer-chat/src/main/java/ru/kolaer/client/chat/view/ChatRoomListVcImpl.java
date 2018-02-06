package ru.kolaer.client.chat.view;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.List;
import java.util.function.Consumer;

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
        roomListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
    public void setOnSelectRoom(Consumer<ChatRoomVc> consumer) {
        roomListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> consumer.accept(newValue));
    }
}
