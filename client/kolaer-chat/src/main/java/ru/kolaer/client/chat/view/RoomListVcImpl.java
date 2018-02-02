package ru.kolaer.client.chat.view;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.Collections;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.02.2018.
 */
public class RoomListVcImpl implements RoomListVc {
    private String subscriptionId;
    private BorderPane mainPane;
    private ListView<ChatRoomDto> roomListView;

    @Override
    public void initView(Consumer<RoomListVc> viewVisit) {
        mainPane = new BorderPane();

        roomListView = new ListView<>();
        roomListView.setPlaceholder(new Label("Wait"));
        roomListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roomListView.setCellFactory(param -> new ListCell<ChatRoomDto>(){
            @Override
            protected void updateItem(ChatRoomDto item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setText("");
                } else {
                    setText(item.getName());
                }
            }
        });

        mainPane.setCenter(roomListView);

        viewVisit.accept(this);
    }

    @Override
    public BorderPane getContent() {
        return mainPane;
    }

    @Override
    public void handlerInfo(ChatInfoDto chatInfoDto) {

    }

    @Override
    public void handlerNewRoom(ChatInfoCreateNewRoomDto chatInfoCreateNewRoomDto) {
        Tools.runOnWithOutThreadFX(() -> roomListView.getItems().add(chatInfoCreateNewRoomDto.getData()));
    }

    @Override
    public void handlerUserAction(ChatInfoUserActionDto chatInfoUserActionDto) {
        ChatUserDto chatUserDto = chatInfoUserActionDto.getData();

        if(chatInfoUserActionDto.getCommand() == ChatInfoCommand.CONNECT) {
            ChatRoomDto chatRoomDto = new ChatRoomDto();
            chatRoomDto.setName(chatUserDto.getName());
            chatRoomDto.setUsers(Collections.singletonList(chatUserDto));
            chatRoomDto.setUserCreated(chatUserDto);

            Tools.runOnWithOutThreadFX(() -> roomListView.getItems().add(chatRoomDto));
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public void setSubscriptionId(String id) {
        subscriptionId = id;
    }

    @Override
    public String getSubscriptionId() {
        return subscriptionId;
    }

    @Override
    public void connect(ChatClient chatClient) {
        chatClient.subscribeInfo(this);
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }
}
