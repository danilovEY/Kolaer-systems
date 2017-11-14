package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.chat.service.ChatClient;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class UserListVcImpl implements UserListVc {
    private BorderPane mainPane;
    private Accordion usersPane;

    @Override
    public void initView(Consumer<UserListVc> viewVisit) {
        mainPane = new BorderPane();
        usersPane = new Accordion();
        mainPane.setCenter(usersPane);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return mainPane;
    }

    @Override
    public void connect(ChatClient chatClient) {
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
            usersPane.getPanes().clear();

            List<ChatGroupDto> response = activeGroup.getResponse();

            TitledPane expandedPane = null;

            AccountDto authorizedUser = UniformSystemEditorKitSingleton.getInstance()
                    .getAuthentication()
                    .getAuthorizedUser();

            for(ChatGroupDto chatGroupDto : response) {
                TitledPane titledPane = new TitledPane();
                titledPane.setText(chatGroupDto.getName());

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

                List<ChatUserDto> chatUsers = chatGroupDto.getUsers()
                        .stream()
                        //.filter(user -> !authorizedUser.getId().equals(user.getAccountId()))
                        .collect(Collectors.toList());

                users.getItems().addAll(chatUsers);

                titledPane.setContent(users);

                Tools.runOnWithOutThreadFX(() -> usersPane.getPanes().add(titledPane));

                if("Main".equals(chatGroupDto.getName())) {
                    expandedPane = titledPane;
                }
            }

            if(expandedPane != null) {
                usersPane.setExpandedPane(expandedPane);
            } else {
                usersPane.getPanes().stream().findFirst().ifPresent(usersPane::setExpandedPane);
            }


        }
    }

    @Override
    public void disconnect(ChatClient chatClient) {

    }
}
