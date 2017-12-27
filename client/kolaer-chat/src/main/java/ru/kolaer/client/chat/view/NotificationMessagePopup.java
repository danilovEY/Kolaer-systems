package ru.kolaer.client.chat.view;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.tools.Tools;

/**
 * Created by danilovey on 11.12.2017.
 */
public class NotificationMessagePopup implements NotificationMessage {
    private final ChatVc chatVc;

    public NotificationMessagePopup(ChatVc chatVc) {
        this.chatVc = chatVc;
    }

    @Override
    public void getMessage(ChatGroupDto chatGroupDto, ChatMessageDto chatMessageDto) {
        Tools.runOnWithOutThreadFX(() -> {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getPopupNotification()
                    .showInformationNotify("У вас новое сообщение!", chatGroupDto.getName());
        });
    }

    @Override
    public void clear() {

    }
}
