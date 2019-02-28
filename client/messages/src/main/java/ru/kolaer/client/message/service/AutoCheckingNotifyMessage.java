package ru.kolaer.client.message.service;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.client.core.mvp.view.BaseView;
import ru.kolaer.client.core.plugins.services.Service;
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.core.system.ui.NotificationView;
import ru.kolaer.client.core.system.ui.StaticView;
import ru.kolaer.client.core.tools.Tools;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by danilovey on 19.08.2016.
 */
@Slf4j
public class AutoCheckingNotifyMessage implements Service, StaticView {
    private final Map<Long, NotificationView> messages = new HashMap<>();
    private boolean run = false;
    private VBox messageListView;
    private Node mainPane;

    @Override
    public boolean isRunning() {
        return this.run;
    }

    @Override
    public String getName() {
        return "Сообщения";
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public void stop() {
        run = false;
    }

    @Override
    public void run() {
        run = true;

        while (run) {
            ServerResponse<PageDto<NotifyMessageDto>> allNotifyMessages = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getNotifyMessageTable()
                    .getAllNotifyMessages();

            if(!allNotifyMessages.isServerError()) {
                Tools.runOnWithOutThreadFX(() -> {
                    if(!isViewInit()) {
                        Tools.runOnWithOutThreadFX(() -> initView(BaseView::empty));

                        allNotifyMessages.getResponse()
                                .getData()
                                .forEach(this::addNotifyMessage);

                        UniformSystemEditorKitSingleton.getInstance()
                                .getUISystemUS()
                                .getStatic().addStaticView(this);
                    } else {
                        allNotifyMessages.getResponse()
                                .getData()
                                .forEach(this::addNotifyMessage);
                    }
                });
            } else {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(allNotifyMessages.getExceptionMessage());
            }

            try {
                TimeUnit.MINUTES.sleep(5);
            } catch (InterruptedException e) {
                run = false;
                return;
            }
        }
    }

    private void addNotifyMessage(NotifyMessageDto notifyMessageDto) {
        if(!messages.containsKey(notifyMessageDto.getId())) {
            NotificationView notificationMessageVc = new NotificationMessageVcImpl(notifyMessageDto);

            messages.put(notifyMessageDto.getId(), notificationMessageVc);

            notificationMessageVc.initView(BaseView::empty);

            Node content = notificationMessageVc.getContent();

            messageListView.getChildren().add(content);

            content.toBack();
        }
    }

    @Override
    public void initView(Consumer<StaticView> viewVisit) {
        messageListView = new VBox();
        messageListView.setMaxHeight(Region.USE_PREF_SIZE);
        messageListView.setSpacing(10);

        setContent(messageListView);

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }

    @Override
    public void setContent(Node content) {
        this.mainPane = content;
    }
}
