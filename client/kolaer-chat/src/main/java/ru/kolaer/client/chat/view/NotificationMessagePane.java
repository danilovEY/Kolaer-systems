package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.api.tools.Tools;

import java.text.SimpleDateFormat;
import java.util.function.Consumer;

/**
 * Created by danilovey on 11.12.2017.
 */
public class NotificationMessagePane implements StaticView, NotificationMessage {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD.MM.yyyy HH:mm:ss");
    private final ChatVc chatVc;
    private BorderPane mainPane;
    private Label dateLabel;
    private Label messageLabel;
    private Label titleLabel;

    public NotificationMessagePane(ChatVc chatVc) {
        this.chatVc = chatVc;
    }

    @Override
    public void initView(Consumer<StaticView> viewVisit) {
        mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #51ffad");

        messageLabel = new Label();
        dateLabel = new Label();
        titleLabel = new Label();

        mainPane.setTop(titleLabel);
        mainPane.setCenter(messageLabel);
        mainPane.setBottom(dateLabel);
    }

    @Override
    public void getMessage(ChatGroupDto chatGroupDto, ChatMessageDto chatMessageDto) {
        Tools.runOnWithOutThreadFX(() -> {
            if(!isViewInit()) {
                this.initView(BaseView::empty);
            }

            titleLabel.setText("У вас новое сообщение!");
            messageLabel.setText(chatMessageDto.getMessage());
            dateLabel.setText(simpleDateFormat.format(chatMessageDto.getCreateMessage()));

            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getStatic()
                    .addStaticView(this);

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

    @Override
    public void clear() {
        Tools.runOnWithOutThreadFX(() -> {
            UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getStatic()
                    .removeStaticView(this);
        });
    }

    @Override
    public Node getContent() {
        return mainPane;
    }
}
