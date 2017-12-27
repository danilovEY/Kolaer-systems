package ru.kolaer.client.chat.view;

import javafx.stage.Stage;
import javafx.util.Duration;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.api.tools.Tools;

import java.util.Collections;

/**
 * Created by danilovey on 11.12.2017.
 */
public class NotificationMessagePopup implements NotificationMessage {
    private final ChatVc chatVc;
    private final UniformSystemPlugin uniformSystemPlugin;

    public NotificationMessagePopup(ChatVc chatVc,
                                    UniformSystemPlugin uniformSystemPlugin) {
        this.chatVc = chatVc;
        this.uniformSystemPlugin = uniformSystemPlugin;
    }

    @Override
    public void getMessage(ChatGroupDto chatGroupDto, ChatMessageDto chatMessageDto) {
        ChatRoomVc chatRoom = chatVc.getChatRoom(chatGroupDto);

        if(!chatVc.roomIsFocus(chatRoom)) {
            Tools.runOnWithOutThreadFX(() -> {
                NotifyAction notifyAction = new NotifyAction("Перейти в " + chatGroupDto.getName(), actionEvent -> {
                    Tools.runOnWithOutThreadFX(() -> {
                        Stage mainStage = UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getMainStage();

                        if(!mainStage.isShowing()) {
                            mainStage.show();
                        }

                        if(mainStage.isIconified()) {
                            mainStage.setIconified(false);
                        }

                        mainStage.toFront();
                        mainStage.requestFocus();

                        UniformSystemEditorKitSingleton.getInstance()
                                .getPluginsUS()
                                .showPlugin(uniformSystemPlugin);

                        chatVc.showChatRoom(chatRoom, true);
                    });
                });

                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getPopupNotification()
                        .showSimpleNotify("У вас новое сообщение!",
                                null,
                                Duration.seconds(5),
                                Collections.singletonList(notifyAction));

                Stage mainStage = UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getMainStage();

                if(!mainStage.isShowing()) {
                    mainStage.show();
                    mainStage.setIconified(true);
                }

                mainStage.toFront();
            });
        }
    }

    @Override
    public void clear() {

    }
}
