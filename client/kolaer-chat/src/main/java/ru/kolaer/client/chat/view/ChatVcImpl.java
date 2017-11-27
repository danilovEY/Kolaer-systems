package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;
import ru.kolaer.client.chat.service.UserListObserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatVcImpl implements ChatVc, UserListObserver {
    private final Map<String, ChatRoomVc> groupDtoMap = new HashMap<>();
    private BorderPane mainPane;
    private TabPane tabPane;
    private ChatClient chatClient;

    @Override
    public void initView(Consumer<ChatVc> viewVisit) {
        mainPane = new BorderPane();

        tabPane = new TabPane();

        mainPane.setCenter(tabPane);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
        this.chatClient = chatClient;
        ServerResponse<List<ChatGroupDto>> activeGroup = UniformSystemEditorKitSingleton.getInstance()
                .getUSNetwork()
                .getKolaerWebServer()
                .getApplicationDataBase()
                .getChatTable()
                .getActiveGroup();

        if(activeGroup.isServerError()) {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getNotification()
                    .showErrorNotify(activeGroup.getExceptionMessage());
        } else {
            activeGroup.getResponse()
                    .forEach(this::createRoom);
        }
    }

    private void createRoom(ChatGroupDto chatGroupDto) {
        Tools.runOnWithOutThreadFX(() -> {
            ChatRoomVc chatRoomVc = new ChatRoomVcImpl(chatGroupDto);
            chatRoomVc.initView(initRoom -> {
                tabPane.getTabs().add(initRoom.getContent());
                if(chatClient.isConnect()) {
                    chatRoomVc.connect(chatClient);
                }
            });
            chatRoomVc.getUserListVc().registerObserver(this);
            chatClient.registerObserver(chatRoomVc);
            groupDtoMap.put(chatGroupDto.getName(), chatRoomVc);
        });
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }

    @Override
    public void addChatRoom(ChatRoomVc chatRoomVc) {
        this.tabPane.getTabs().add(chatRoomVc.getContent());
    }

    @Override
    public void connectUser(ChatUserDto chatUserDto) {

    }

    @Override
    public void disconnectUser(ChatUserDto chatUserDto) {

    }

    @Override
    public void selected(ChatUserDto chatUserDto) {
        ChatGroupDto chatGroupDto = new ChatGroupDto();
        chatGroupDto.setUsers(Arrays.asList(chatUserDto));
        chatGroupDto.setName(chatUserDto.getRoomName());

        this.createRoom(chatGroupDto);
    }
}
