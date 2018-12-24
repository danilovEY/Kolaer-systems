package ru.kolaer.client.chat.view;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatGroupType;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.plugins.UniformSystemPlugin;
import ru.kolaer.common.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.common.system.ui.NotificationType;
import ru.kolaer.common.system.ui.NotificationView;
import ru.kolaer.common.system.ui.NotifyAction;
import ru.kolaer.common.tools.Tools;

import java.util.Collections;

/**
 * Created by danilovey on 11.12.2017.
 */
public class NotificationMessagePopup implements NotificationMessage {
    private final ChatContentVc chatContentVc;
    private final UniformSystemPlugin uniformSystemPlugin;
    private Button notifyAction;
    private NotificationView notifyView;
    private Integer unreadCountMessage = 1;

    public NotificationMessagePopup(ChatContentVc chatContentVc,
                                    UniformSystemPlugin uniformSystemPlugin) {
        this.chatContentVc = chatContentVc;
        this.uniformSystemPlugin = uniformSystemPlugin;
        this.notifyView = UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getPopupNotification()
                .createNotify();
    }

    @Override
    public void receivedMessage(ChatRoomVc chatRoomVc, ChatMessageDto chatMessageDto) {
        Long authId = UniformSystemEditorKitSingleton.getInstance()
                .getAuthentication()
                .getAuthorizedUser()
                .getId();

        Stage mainStage = UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getMainStage();


        if(mainStage.isShowing() && mainStage.isFocused() && !mainStage.isIconified() &&
                chatRoomVc.isSelected() ||
                authId.equals(chatMessageDto.getFromAccount().getAccountId())) {
            return;
        }

        String message = "Сообщение " +
                (chatRoomVc.getChatRoomDto().getType() == ChatGroupType.SINGLE ? "от " : "из ")
                + chatRoomVc.getChatRoomDto().getName();

        Tools.runOnWithOutThreadFX(() -> {
                NotifyAction openChatAction = new NotifyAction(message, actionEvent -> {
                    Tools.runOnWithOutThreadFX(() -> {
                        mainStage.show();
                        mainStage.setIconified(false);
                        mainStage.requestFocus();

                        UniformSystemEditorKitSingleton.getInstance()
                                .getPluginsUS()
                                .showPlugin(uniformSystemPlugin);

                        chatContentVc.showChatRoom(chatRoomVc);

                        if(notifyView != null && notifyView.isShow()) {
                            notifyView.hide();
                        }
                    });
                });

                if(notifyView == null) {
                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getPopupNotification()
                            .showSimpleNotify("Сообщение",
                                    "У вас новое сообщение!",
                                    Duration.hours(1),
                                    Collections.singletonList(openChatAction));
                } else {
                    if(!notifyView.isViewInit()) {
                        notifyView.setDuration(Duration.hours(1));
                        notifyView.setType(NotificationType.INFO);
                        notifyView.setOnClose(e -> unreadCountMessage = 1);

                        notifyAction = new Button("Открыть чат");
                        notifyAction.setMaxWidth(Double.MAX_VALUE);

                        notifyView.setContent(notifyAction);
                        notifyView.initView(BaseView::empty);
                    }

                    notifyAction.setOnAction(openChatAction.getConsumer()::accept);
                    notifyView.setText(openChatAction.getText());

                    if(!notifyView.isShow()) {
                        notifyView.setTitle("У вас новое сообщение!");
                        notifyView.show();
                    } else {
                        notifyView.setTitle("У вас (" + ++unreadCountMessage + ") новых сообщений!");
                    }
                }
        });
    }

    @Override
    public void clear() {

    }
}
