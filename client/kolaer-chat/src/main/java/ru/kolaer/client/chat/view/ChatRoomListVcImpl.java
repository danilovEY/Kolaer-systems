package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.IdDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
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
    private BorderPane mainPane;
    private ListView<ChatRoomVc> roomListView;
    private ChatClient chatClient;

    @Override
    public void initView(Consumer<ChatRoomListVc> viewVisit) {
        mainPane = new BorderPane();

        roomListView = new ListView<>();
        roomListView.setPlaceholder(new Label("Wait"));
        roomListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roomListView.setCellFactory(param -> new ListCell<ChatRoomVc>(){
            @Override
            protected void updateItem(ChatRoomVc item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(item.getContent());
                }
            }
        });

        mainPane.setCenter(roomListView);

        viewVisit.accept(this);
    }

    private ChatRoomVc convertToChatRoomDto(ChatRoomDto chatRoomDto) {
        ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatRoomDto);
        chatRoomVc.initView(BaseView::empty);

        chatRoomVc.connect(chatClient);

        return chatRoomVc;
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void handlerInfo(ChatInfoUserActionDto infoUserActionDto) {
        if(infoUserActionDto.getCommand() == ChatInfoCommand.CONNECT) {
            ChatUserDto chatUserDto = infoUserActionDto.getChatUserDto();

            if(!checkUserRoom(chatUserDto)) {
                ServerResponse<ChatRoomDto> singleGroup = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .createSingleRoom(new IdDto(chatUserDto.getAccountId()));

                if(singleGroup.isServerError()) {
                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getNotification()
                            .showErrorNotify(singleGroup.getExceptionMessage());
                }
            }
        }
    }

    @Override
    public void handlerInfo(ChatInfoRoomActionDto chatInfoRoomActionDto) {
        addChatRoomDto(chatInfoRoomActionDto.getChatRoomDto());
    }

    @Override
    public void addChatRoomDto(List<ChatRoomDto> chatRoomDtoList) {
        roomListView.getItems().addAll(chatRoomDtoList.stream().map(this::convertToChatRoomDto).collect(Collectors.toList()));
    }

    @Override
    public void addChatRoomDto(ChatRoomDto chatRoomDto) {
        roomListView.getItems().add(this.convertToChatRoomDto(chatRoomDto));
    }

    private boolean checkUserRoom(ChatUserDto chatUserDto) {
        for (ChatRoomVc chatRoomVc : roomListView.getItems()) {
            ChatRoomDto chatRoomDto = chatRoomVc.getChatRoomDto();

            boolean present = chatRoomDto.getUsers()
                    .stream()
                    .anyMatch(user -> user.getAccountId().equals(chatUserDto.getAccountId()));

            if(present) {
                return true;
            }
        }

        return false;
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
