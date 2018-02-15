package ru.kolaer.client.usa.services;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.ui.NotificationView;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.system.ui.NotificationMessageVcImpl;

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

        if(!isViewInit()) {
            Tools.runOnWithOutThreadFX(() -> initView(UniformSystemEditorKitSingleton.getInstance()
                    .getUISystemUS()
                    .getStatic()::addStaticView));
        }

        while (run) {
            ServerResponse<Page<NotifyMessageDto>> allNotifyMessages = UniformSystemEditorKitSingleton.getInstance()
                    .getUSNetwork()
                    .getKolaerWebServer()
                    .getApplicationDataBase()
                    .getNotifyMessageTable()
                    .getAllNotifyMessages();

            if(!allNotifyMessages.isServerError()) {
                Tools.runOnWithOutThreadFX(() -> allNotifyMessages.getResponse()
                        .getData()
                        .forEach(this::addNotifyMessage));
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
