package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class UserListVcImpl implements UserListVc {
    private Map<Long, ListView<ChatUserDto>> usersMap = new HashMap<>();
    private BorderPane mainPane;

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();

        ListView<ChatUserDto> users = new ListView<>();
        users.setCellFactory(param -> new ListCell<ChatUserDto>(){
            @Override
            protected void updateItem(ChatUserDto item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setText("");
                } else {
                    setText(item.getName());
                }
            }
        });

        mainPane.setCenter(users);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {


       /* chatClient.subscribeInfo(new ChatInfoHandler() {
            @Override
            public void handleFrame(StompHeaders headers, ChatInfoDto info) {
                log.info(info.toString());
                ServerResponse<List<ChatGroupDto>> activeGroup = UniformSystemEditorKitSingleton.getInstance()
                        .getUSNetwork()
                        .getKolaerWebServer()
                        .getApplicationDataBase()
                        .getChatTable()
                        .getActiveGroup();

                log.info(activeGroup.toString());

                if(activeGroup.isServerError()) {
                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getNotification()
                            .showErrorNotify(activeGroup.getExceptionMessage());
                } else {
                    for (ChatGroupDto chatGroupDto : activeGroup.getResponse()) {
                        if(usersMap.containsKey(chatGroupDto.getId())) {
                            ListView<ChatUserDto> chatUserDtoListView = usersMap.get(chatGroupDto.getId());
                            Tools.runOnWithOutThreadFX(() -> {
                                chatUserDtoListView.getItems().setAll(chatGroupDto.getUsers());
                            });
                        }
                    }
                }
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {

            }
        });*/
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }
}
